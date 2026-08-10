import urllib.request
import time
import json

for i in range(1, 12):
    payload = json.dumps({"id": i, "data": f"test-data-{i}"}).encode('utf-8')
    print(f"Enviando payload: {payload.decode('utf-8')}")
    req = urllib.request.Request("http://localhost:8080/process", data=payload, headers={'Content-Type': 'application/json'})
    try:
        with urllib.request.urlopen(req) as response:
            print(f"Resposta: {response.read().decode('utf-8')}")
    except Exception as e:
        print(f"Erro no envio do payload {i}: {e}")
    time.sleep(1)
