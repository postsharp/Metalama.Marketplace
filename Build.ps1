(& dotnet nuget locals http-cache -c) | Out-Null
& dotnet run --project "$PSScriptRoot\eng\src\BuildMyProduct.csproj" -- $args
exit $LASTEXITCODE

