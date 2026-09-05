# AdaptiveBroker

The AdaptiveBroker extends the default CloudSim Plus broker.

## Constructor

```java
AdaptiveBroker(simulation,null);
```

Automatic prediction.

```java
AdaptiveBroker(simulation,new RoundRobinBroker());
```

Manual policy.

---

## Example

```java
AdaptiveBroker broker =
        new AdaptiveBroker((simulation,null);
```

---

## Runtime Adaptation

The broker continuously monitors

- CPU utilization

- Response time

- Throughput

- Migration time

- Resource utilization

When thresholds are exceeded, the framework automatically requests a new prediction from IA-Cloud.
