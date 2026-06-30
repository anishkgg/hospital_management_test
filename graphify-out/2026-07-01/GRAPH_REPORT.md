# Graph Report - hospital_management_test  (2026-06-30)

## Corpus Check
- 42 files · ~8,646 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 341 nodes · 671 edges · 19 communities (12 shown, 7 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 32 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `15e6982e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]

## God Nodes (most connected - your core abstractions)
1. `AppointmentResponseDTO` - 25 edges
2. `Doctor` - 23 edges
3. `controller` - 21 edges
4. `Appointment` - 21 edges
5. `DoctorDetailsResponseDTO` - 19 edges
6. `Hospital` - 17 edges
7. `AppointmentServiceImpl` - 15 edges
8. `AppointmentReportRepository` - 15 edges
9. `DoctorResponseDTO` - 14 edges
10. `HospitalResponseDTO` - 14 edges

## Surprising Connections (you probably didn't know these)
- `hospitalmanagementtest (debug)` --semantically_similar_to--> `hospitalmanagementtest`  [INFERRED] [semantically similar]
  compose.debug.yaml → compose.yaml
- `controller` --references--> `AppointmentService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/AppointmentService.java
- `controller` --references--> `DoctorService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/DoctorService.java
- `controller` --references--> `ReportService`  [EXTRACTED]
  src/main/java/com/hospital/Controller/controller.java → src/main/java/com/hospital/Service/ReportService.java
- `Appointment` --references--> `AppointmentStatus`  [EXTRACTED]
  src/main/java/com/hospital/entity/Appointment.java → src/main/java/com/hospital/Enum/AppointmentStatus.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Container Deployment Configuration** — compose_debug_hospitalmanagementtest, compose_hospitalmanagementtest, dockerfile [EXTRACTED 1.00]
- **Graphify Agent Tooling** — agents_rules_graphify_rule, agents_workflows_graphify_workflow [EXTRACTED 1.00]

## Communities (19 total, 7 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (34): Pattern, DoctorRegistrationDTO, Builder, Double, LocalTime, Long, String, DoctorRequestDTO (+26 more)

### Community 1 - "Community 1"
Cohesion: 0.10
Nodes (21): GetMapping, PostMapping, PutMapping, RequestMapping, RestController, controller, List, Long (+13 more)

### Community 2 - "Community 2"
Cohesion: 0.10
Nodes (21): Optional, SecureRandom, Appointment, AllArgsConstructor, Builder, Data, Entity, LocalDateTime (+13 more)

### Community 3 - "Community 3"
Cohesion: 0.07
Nodes (38): ApplicationEventPublisher, IllegalArgumentException, AppointmentCompleteRequestDTO, String, AppointmentRequestDTO, LocalDateTime, Long, String (+30 more)

### Community 4 - "Community 4"
Cohesion: 0.13
Nodes (17): Pageable, HospitalReportDTO, Builder, Double, Long, String, AppointmentReportRepository, List (+9 more)

### Community 5 - "Community 5"
Cohesion: 0.13
Nodes (18): Doctor, AllArgsConstructor, Builder, Data, Double, Entity, List, LocalTime (+10 more)

### Community 8 - "Community 8"
Cohesion: 0.36
Nodes (4): Connection, Properties, DatabaseConnection, String

### Community 9 - "Community 9"
Cohesion: 0.46
Nodes (5): Async, Component, EventListener, Logger, NotificationEventListener

### Community 10 - "Community 10"
Cohesion: 0.39
Nodes (6): ExceptionHandler, Map, RestControllerAdvice, GlobalExceptionHandler, ResponseEntity, String

### Community 11 - "Community 11"
Cohesion: 0.47
Nodes (4): EnableAsync, SpringBootApplication, HospitalManagementApplication, String

### Community 19 - "Community 19"
Cohesion: 0.10
Nodes (15): applyRoleStyles(), drawAnalyticsChart(), fetchAppointments(), fetchDoctors(), fetchHospitals(), formatDateTime(), initRoleSwitch(), loadAllData() (+7 more)

### Community 20 - "Community 20"
Cohesion: 0.10
Nodes (21): JpaRepository, Hospital, AllArgsConstructor, Builder, Data, Double, Entity, List (+13 more)

## Knowledge Gaps
- **7 isolated node(s):** `com.hospital:hospital-management-test`, `state`, `graphify`, `Workflow: graphify`, `My Project` (+2 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **7 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Doctor` connect `Community 5` to `Community 0`, `Community 2`, `Community 3`, `Community 20`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Why does `AppointmentResponseDTO` connect `Community 3` to `Community 1`?**
  _High betweenness centrality (0.085) - this node is a cross-community bridge._
- **Why does `Appointment` connect `Community 2` to `Community 3`, `Community 4`, `Community 5`?**
  _High betweenness centrality (0.074) - this node is a cross-community bridge._
- **What connects `com.hospital:hospital-management-test`, `state`, `graphify` to the rest of the system?**
  _7 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.06516290726817042 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.10365853658536585 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.1010752688172043 - nodes in this community are weakly interconnected._