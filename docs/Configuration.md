# Configuration

Create

```
iacloud.properties
```

Example

```properties
api.url=https://api.iacloud.ai/api/predict-array

api.key=pk_live_xxxxxxxxxxxxx

policy=null

adaptive.enabled=true

fallback.policy=RoundRobin
```

If policy=null the framework automatically predicts the initial algorithm.