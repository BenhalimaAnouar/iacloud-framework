# Configuration

Create or copy conf.properties.example

```
conf.properties
```

Example

```properties
api.url=https://api.iacloud.ai/api/predict-array

api.key=pk_live_xxxxxxxxxxxxx


```

# Example Configuration

```properties
############################################################
# IA-Cloud Prediction API
############################################################

api=https://iacloudac.ma/api/predictLb
api_key=YOUR-API-KEY

############################################################
# Initial QoS Requirements
############################################################

Performance=yes
Throughtput=no
Overhead=yes
Tolerant=yes
MigrationTime=yes
ResponseTime=yes
RessourceUtilization=no
Scalability=yes
PowerSaving=no

############################################################
# Adaptive Monitoring Parameters
############################################################

toleranceMax=2
deadline=1000
tolerance=100
timeMax=10000
throughputMax=0.00002
responseTimeMAX=0.4
ressourceCpuMax=80
```

---

# Configuration Parameters

## Prediction API

| Property | Description |
|-----------|-------------|
| `api` | IA-Cloud Prediction API endpoint. |
| `api_key` | API Key generated from the IA-Cloud Developer Dashboard. |

---

## QoS Requirements

These parameters represent the user's desired Quality of Service (QoS). The framework sends them to the IA-Cloud Prediction API before starting the simulation.

Possible values:

- `yes`
- `no`

| Property | Description |
|-----------|-------------|
| `Performance` | High overall system performance. |
| `Throughtput` | Maximize throughput. |
| `Overhead` | Minimize load balancing overhead. |
| `Tolerant` | Increase fault tolerance. |
| `MigrationTime` | Reduce virtual machine migration time. |
| `ResponseTime` | Minimize response time. |
| `RessourceUtilization` | Improve resource utilization. |
| `Scalability` | Support scalable cloud infrastructures. |
| `PowerSaving` | Optimize energy consumption. |

Example:

```properties
Performance=yes
Throughtput=no
Overhead=yes
Tolerant=yes
MigrationTime=yes
ResponseTime=yes
RessourceUtilization=no
Scalability=yes
PowerSaving=no
```

# Adaptive Monitoring

After the initial prediction, the framework continuously monitors the cloud environment.

If one or more thresholds are exceeded, IA-Cloud automatically requests a new prediction and dynamically changes the load balancing algorithm.

| Property | Description |
|-----------|-------------|
| `toleranceMax` | Maximum number of tolerated violations before adaptation. |
| `deadline` | Maximum acceptable task deadline (milliseconds). |
| `tolerance` | Allowed execution tolerance. |
| `timeMax` | Maximum execution time. |
| `throughputMax` | Throughput threshold used for adaptation. |
| `responseTimeMAX` | Maximum acceptable response time (seconds). |
| `ressourceCpuMax` | Maximum CPU utilization (%) before adaptation. |

---

# Adaptive Workflow

```text
Load Configuration
        │
        ▼
Read QoS Requirements
        │
        ▼
Authenticate using API Key
        │
        ▼
Call IA-Cloud Prediction API
        │
        ▼
Receive Best Load Balancing Algorithm
        │
        ▼
Initialize AdaptiveBroker
        │
        ▼
Start Cloud Simulation
        │
        ▼
Monitor QoS Metrics
        │
        ▼
Threshold Exceeded?
        │
     Yes ▼ No
        │
        ▼
Request New Prediction
        │
        ▼
Switch Load Balancing Algorithm
```

---

# Security

> **Important**

The API Key uniquely identifies your application.

Never expose your production API Key in public repositories.

For production deployments, it is recommended to store sensitive credentials using environment variables or a secure secrets manager instead of committing them to version control.

---

# Default Values

If a property is missing, the framework uses its built-in default configuration.

Users can override any parameter by modifying the `iacloud.properties` file before starting the simulation.
