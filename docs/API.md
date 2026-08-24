# Prediction API

Endpoint

POST

```
https://api.iacloud.ai/api/predictLb
```

Headers

```
X-API-Key: pk_live_xxxxxxxxx
```

Request

```json
{
  "array_param":[1,0,1,1,0,1,0,1,1]
}
```

Response

```json
{
  "success":true,
  "result":"PALB",
  "accuracy":98.7
}
```