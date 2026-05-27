$ErrorActionPreference = "Stop"

$root = Join-Path $PSScriptRoot "flight-app\dist"
$backend = "http://localhost:8080"
$prefix = "http://localhost:5173/"

Add-Type -AssemblyName System.Net.Http

function Get-ContentType {
    param([string] $Path)

    switch ([System.IO.Path]::GetExtension($Path).ToLowerInvariant()) {
        ".html" { "text/html; charset=utf-8" }
        ".js" { "application/javascript; charset=utf-8" }
        ".css" { "text/css; charset=utf-8" }
        ".json" { "application/json; charset=utf-8" }
        ".svg" { "image/svg+xml" }
        ".png" { "image/png" }
        ".jpg" { "image/jpeg" }
        ".jpeg" { "image/jpeg" }
        ".ico" { "image/x-icon" }
        default { "application/octet-stream" }
    }
}

$listener = [System.Net.HttpListener]::new()
$listener.Prefixes.Add($prefix)
$listener.Start()

$client = [System.Net.Http.HttpClient]::new()
Write-Host "Flight app listening at $prefix"

while ($listener.IsListening) {
    $context = $listener.GetContext()
    $request = $context.Request
    $response = $context.Response

    try {
        if ($request.Url.AbsolutePath.StartsWith("/api/")) {
            $target = "$backend$($request.RawUrl)"
            $message = [System.Net.Http.HttpRequestMessage]::new(
                [System.Net.Http.HttpMethod]::new($request.HttpMethod),
                $target
            )

            if ($request.HasEntityBody) {
                $body = [System.IO.MemoryStream]::new()
                $request.InputStream.CopyTo($body)
                $content = [System.Net.Http.ByteArrayContent]::new($body.ToArray())
                if ($request.ContentType) {
                    $content.Headers.TryAddWithoutValidation("Content-Type", $request.ContentType) | Out-Null
                }
                $message.Content = $content
            }

            $backendResponse = $client.SendAsync($message).GetAwaiter().GetResult()
            $bytes = $backendResponse.Content.ReadAsByteArrayAsync().GetAwaiter().GetResult()
            $response.StatusCode = [int] $backendResponse.StatusCode

            if ($backendResponse.Content.Headers.ContentType) {
                $response.ContentType = $backendResponse.Content.Headers.ContentType.ToString()
            }

            $response.OutputStream.Write($bytes, 0, $bytes.Length)
            continue
        }

        $relativePath = [System.Uri]::UnescapeDataString($request.Url.AbsolutePath.TrimStart("/"))
        if ([string]::IsNullOrWhiteSpace($relativePath)) {
            $relativePath = "index.html"
        }

        $filePath = Join-Path $root $relativePath
        $fullPath = [System.IO.Path]::GetFullPath($filePath)
        $fullRoot = [System.IO.Path]::GetFullPath($root)

        if (-not $fullPath.StartsWith($fullRoot, [System.StringComparison]::OrdinalIgnoreCase) -or
            -not [System.IO.File]::Exists($fullPath)) {
            $fullPath = Join-Path $root "index.html"
        }

        $bytes = [System.IO.File]::ReadAllBytes($fullPath)
        $response.StatusCode = 200
        $response.ContentType = Get-ContentType $fullPath
        $response.OutputStream.Write($bytes, 0, $bytes.Length)
    }
    catch {
        $message = [System.Text.Encoding]::UTF8.GetBytes($_.Exception.Message)
        $response.StatusCode = 500
        $response.ContentType = "text/plain; charset=utf-8"
        $response.OutputStream.Write($message, 0, $message.Length)
    }
    finally {
        $response.OutputStream.Close()
    }
}
