# Graph Report - .  (2026-06-30)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 299 nodes · 621 edges · 16 communities (12 shown, 4 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 33 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `fa97ade9`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]

## God Nodes (most connected - your core abstractions)
1. `AppointmentResponseDTO` - 25 edges
2. `Doctor` - 22 edges
3. `controller` - 21 edges
4. `Appointment` - 21 edges
5. `DoctorDetailsResponseDTO` - 18 edges
6. `Hospital` - 16 edges
7. `AppointmentServiceImpl` - 15 edges
8. `AppointmentReportRepository` - 15 edges
9. `AppointmentRepository` - 14 edges
10. `DoctorResponseDTO` - 13 edges

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

## Communities (16 total, 4 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (30): Pattern, DoctorRegistrationDTO, Builder, LocalTime, Long, String, DoctorRequestDTO, Builder (+22 more)

### Community 1 - "Community 1"
Cohesion: 0.11
Nodes (19): GetMapping, PostMapping, PutMapping, RequestMapping, RestController, controller, List, Long (+11 more)

### Community 2 - "Community 2"
Cohesion: 0.10
Nodes (21): Optional, SecureRandom, Appointment, AllArgsConstructor, Builder, Data, Entity, LocalDateTime (+13 more)

### Community 3 - "Community 3"
Cohesion: 0.11
Nodes (20): AppointmentRequestDTO, LocalDateTime, Long, String, AppointmentRescheduleRequestDTO, LocalDateTime, AppointmentBookingResponseDTO, Builder (+12 more)

### Community 4 - "Community 4"
Cohesion: 0.13
Nodes (18): ApplicationEventPublisher, IllegalArgumentException, AppointmentCompleteRequestDTO, String, AppointmentBookedEvent, LocalDateTime, Long, String (+10 more)

### Community 5 - "Community 5"
Cohesion: 0.13
Nodes (17): Double, Pageable, HospitalReportDTO, Builder, Long, String, AppointmentReportRepository, List (+9 more)

### Community 6 - "Community 6"
Cohesion: 0.11
Nodes (19): Hospital, AllArgsConstructor, Builder, Data, Entity, List, Long, NoArgsConstructor (+11 more)

### Community 7 - "Community 7"
Cohesion: 0.13
Nodes (18): JpaRepository, Doctor, AllArgsConstructor, Builder, Data, Entity, List, LocalTime (+10 more)

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

## Knowledge Gaps
- **4 isolated node(s):** `com.hospital:hospital-management-test`, `My Project`, `Graphify Agent Rule`, `Graphify Workflow`
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Doctor` connect `Community 7` to `Community 0`, `Community 2`, `Community 4`, `Community 6`?**
  _High betweenness centrality (0.144) - this node is a cross-community bridge._
- **Why does `AppointmentResponseDTO` connect `Community 3` to `Community 1`, `Community 4`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **Why does `Appointment` connect `Community 2` to `Community 3`, `Community 4`, `Community 5`, `Community 7`?**
  _High betweenness centrality (0.093) - this node is a cross-community bridge._
- **What connects `com.hospital:hospital-management-test`, `My Project`, `Graphify Agent Rule` to the rest of the system?**
  _4 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.07256894049346879 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.11201079622132254 - nodes in this community are weakly interconnected._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.1010752688172043 - nodes in this community are weakly interconnected._