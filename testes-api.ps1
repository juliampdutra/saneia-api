# ==========================================================
# Saneia API - roteiro automático de testes (todos os status)
# Uso local : powershell -ExecutionPolicy Bypass -File .\testes-api.ps1
# Uso Azure : powershell -ExecutionPolicy Bypass -File .\testes-api.ps1 -BaseUrl https://SUA-URL
# ==========================================================
param([string]$BaseUrl = "http://localhost:8080")

$api = "$BaseUrl/api/v1"
$script:ok = 0
$script:falhas = 0
$sufixo = Get-Date -Format "MMddHHmmss"   # deixa nomes/e-mails únicos a cada execução

Add-Type -AssemblyName System.Net.Http
$handler = New-Object System.Net.Http.HttpClientHandler
$handler.AllowAutoRedirect = $true
$client = New-Object System.Net.Http.HttpClient($handler)
$client.Timeout = [TimeSpan]::FromSeconds(90)

# HttpClient não lança exceção em 4xx/5xx: sempre temos status e corpo da resposta
function Chamar([string]$metodo, [string]$url, [string]$corpo = $null) {
    $req = New-Object System.Net.Http.HttpRequestMessage((New-Object System.Net.Http.HttpMethod($metodo)), $url)
    if ($corpo) {
        $req.Content = New-Object System.Net.Http.StringContent($corpo, [System.Text.Encoding]::UTF8, "application/json")
    }
    try {
        $resp = $client.SendAsync($req).GetAwaiter().GetResult()
    } catch {
        Write-Host "Não foi possível conectar em $url - a API está rodando?" -ForegroundColor Red
        exit 1
    }
    $status = [int]$resp.StatusCode
    $bytes = $resp.Content.ReadAsByteArrayAsync().GetAwaiter().GetResult()
    $texto = [System.Text.Encoding]::UTF8.GetString($bytes)
    $json = $null
    if ($texto) { try { $json = $texto | ConvertFrom-Json } catch { } }
    return [pscustomobject]@{ Status = $status; Json = $json; Texto = $texto }
}

function Teste($num, $descricao, $esperado, $res) {
    if ($res.Status -eq $esperado) {
        Write-Host ("[OK]    {0,2}. {1} -> {2}" -f $num, $descricao, $res.Status) -ForegroundColor Green
        $script:ok++
    } else {
        Write-Host ("[FALHA] {0,2}. {1} -> esperado {2}, veio {3}" -f $num, $descricao, $esperado, $res.Status) -ForegroundColor Red
        if ($res.Texto) { Write-Host "         resposta: $($res.Texto)" -ForegroundColor DarkGray }
        $script:falhas++
    }
}

function Confere($num, $descricao, [bool]$condicao, $detalhe) {
    if ($condicao) {
        Write-Host ("[OK]    {0,2}. {1}" -f $num, $descricao) -ForegroundColor Green
        $script:ok++
    } else {
        Write-Host ("[FALHA] {0,2}. {1} (veio: {2})" -f $num, $descricao, $detalhe) -ForegroundColor Red
        $script:falhas++
    }
}

Write-Host ""
Write-Host "Testando $BaseUrl" -ForegroundColor Cyan
Write-Host "---------------- SUCESSO ----------------" -ForegroundColor Cyan

# 1. Swagger na raiz
$r = Chamar "GET" "$BaseUrl/"
Teste 1 "GET / (redireciona para o Swagger)" 200 $r

# 2-4. Distrito
$distrito = "{`"nome`":`"Se Teste $sufixo`",`"zona`":`"CENTRO`",`"subprefeitura`":`"Se`",`"codigoIbge`":`"355030877`",`"populacaoEstimada`":23651,`"areaKm2`":2.1}"
$r = Chamar "POST" "$api/distritos" $distrito
Teste 2 "POST /distritos" 201 $r
$distritoId = $r.Json.id

$r = Chamar "GET" "$api/distritos"
Teste 3 "GET /distritos (listar)" 200 $r

$r = Chamar "GET" "$api/distritos/$distritoId"
Teste 4 "GET /distritos/$distritoId" 200 $r

$distritoPut = "{`"nome`":`"Se Teste $sufixo`",`"zona`":`"CENTRO`",`"subprefeitura`":`"Se`",`"populacaoEstimada`":24000,`"areaKm2`":2.1}"
$r = Chamar "PUT" "$api/distritos/$distritoId" $distritoPut
Teste 5 "PUT /distritos/$distritoId" 200 $r

# 6-8. Morador
$email = "maria.$sufixo@email.com"
$morador = "{`"nome`":`"Maria da Silva`",`"email`":`"$email`",`"telefone`":`"11987654321`",`"cep`":`"01001-000`",`"logradouro`":`"Praca da Se, 100`",`"distritoId`":$distritoId}"
$r = Chamar "POST" "$api/moradores" $morador
Teste 6 "POST /moradores" 201 $r
$moradorId = $r.Json.id

$r = Chamar "GET" "$api/moradores/$moradorId"
Teste 7 "GET /moradores/$moradorId" 200 $r

$moradorPut = "{`"nome`":`"Maria da Silva`",`"email`":`"$email`",`"telefone`":`"11911112222`",`"distritoId`":$distritoId}"
$r = Chamar "PUT" "$api/moradores/$moradorId" $moradorPut
Teste 8 "PUT /moradores/$moradorId" 200 $r

# 9-10. Ocorrência
$ocorrencia = "{`"titulo`":`"Vazamento na calcada`",`"descricao`":`"Agua escorrendo ha 2 dias`",`"tipo`":`"VAZAMENTO`",`"prioridade`":`"ALTA`",`"enderecoReferencia`":`"Rua Direita, 250`",`"moradorId`":$moradorId,`"distritoId`":$distritoId}"
$r = Chamar "POST" "$api/ocorrencias" $ocorrencia
Teste 9 "POST /ocorrencias" 201 $r
$ocorrenciaId = $r.Json.id
Confere 10 "Ocorrência nasce com status ABERTA" ($r.Json.status -eq "ABERTA") $r.Json.status

# 11-13. Atendimento
$atendimento = "{`"equipeResponsavel`":`"Equipe Centro 03`",`"dataAgendada`":`"2026-11-20T09:00:00`",`"descricaoServico`":`"Troca da tubulacao`",`"custoEstimado`":350.00,`"ocorrenciaId`":$ocorrenciaId}"
$r = Chamar "POST" "$api/atendimentos" $atendimento
Teste 11 "POST /atendimentos" 201 $r
$atendimentoId = $r.Json.id

$r = Chamar "GET" "$api/ocorrencias/$ocorrenciaId"
Confere 12 "Ocorrência passou para EM_ATENDIMENTO" ($r.Json.status -eq "EM_ATENDIMENTO") $r.Json.status

$atendimentoPut = "{`"equipeResponsavel`":`"Equipe Centro 03`",`"dataAgendada`":`"2026-11-20T09:00:00`",`"status`":`"CONCLUIDO`",`"custoEstimado`":380.00,`"ocorrenciaId`":$ocorrenciaId}"
$r = Chamar "PUT" "$api/atendimentos/$atendimentoId" $atendimentoPut
Teste 13 "PUT /atendimentos/$atendimentoId (CONCLUIDO)" 200 $r

$r = Chamar "GET" "$api/ocorrencias/$ocorrenciaId"
Confere 14 "Ocorrência passou para RESOLVIDA" ($r.Json.status -eq "RESOLVIDA") $r.Json.status

$r = Chamar "GET" "$api/atendimentos"
Teste 15 "GET /atendimentos (listar)" 200 $r

$r = Chamar "GET" "$api/ocorrencias"
Teste 16 "GET /ocorrencias (listar)" 200 $r

$r = Chamar "GET" "$api/moradores"
Teste 17 "GET /moradores (listar)" 200 $r

Write-Host "---------------- ERROS ----------------" -ForegroundColor Cyan

$r = Chamar "GET" "$api/distritos/999999"
Teste 18 "GET /distritos/999999 (inexistente)" 404 $r

$r = Chamar "GET" "$api/distritos/abc"
Teste 19 "GET /distritos/abc (ID inválido)" 400 $r

$r = Chamar "POST" "$api/moradores" "{`"nome`":`"`",`"email`":`"x`",`"telefone`":`"1`",`"distritoId`":$distritoId}"
Teste 20 "POST /moradores com campos inválidos" 400 $r
Confere 21 "Erro 400 lista os campos inválidos" ($r.Json.campos.Count -ge 3) $r.Texto

$r = Chamar "POST" "$api/distritos" "{`"nome`":`"Teste Enum $sufixo`",`"zona`":`"NORDESTE`",`"subprefeitura`":`"X`"}"
Teste 22 "POST /distritos com enum inválido" 400 $r

$r = Chamar "POST" "$api/distritos" "{ `"nome`": "
Teste 23 "POST /distritos com JSON malformado" 400 $r

$r = Chamar "POST" "$api/moradores" $morador
Teste 24 "POST /moradores com e-mail duplicado" 409 $r

$r = Chamar "POST" "$api/moradores" "{`"nome`":`"Joao`",`"email`":`"joao.$sufixo@email.com`",`"telefone`":`"11900000000`",`"distritoId`":999999}"
Teste 25 "POST /moradores com distritoId inexistente" 404 $r

$r = Chamar "PUT" "$api/moradores/999999" $moradorPut
Teste 26 "PUT /moradores/999999 (inexistente)" 404 $r

$r = Chamar "DELETE" "$api/ocorrencias/999999"
Teste 27 "DELETE /ocorrencias/999999 (inexistente)" 404 $r

$r = Chamar "DELETE" "$api/distritos/$distritoId"
Teste 28 "DELETE /distritos/$distritoId com vínculos" 409 $r

$r = Chamar "POST" "$api/atendimentos" $atendimento
Teste 29 "POST /atendimentos p/ ocorrência RESOLVIDA (regra)" 422 $r

$r = Chamar "GET" "$api/rota-que-nao-existe"
Teste 30 "GET rota inexistente" 404 $r

Write-Host "---------------- EXCLUSÃO ----------------" -ForegroundColor Cyan

$r = Chamar "POST" "$api/distritos" "{`"nome`":`"Temporario $sufixo`",`"zona`":`"OESTE`",`"subprefeitura`":`"Pinheiros`"}"
Teste 31 "POST /distritos (temporário)" 201 $r
$tempId = $r.Json.id

$r = Chamar "DELETE" "$api/distritos/$tempId"
Teste 32 "DELETE /distritos/$tempId" 204 $r

$r = Chamar "GET" "$api/distritos/$tempId"
Teste 33 "GET /distritos/$tempId após exclusão" 404 $r

Write-Host ""
$cor = if ($script:falhas -eq 0) { "Green" } else { "Yellow" }
Write-Host "RESULTADO: $($script:ok) OK | $($script:falhas) FALHA(S)" -ForegroundColor $cor
