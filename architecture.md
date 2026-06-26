# Architecture and Business Context: Ticket Booking System

## Description
High-concurrency event ticketing platform designed to handle massive traffic spikes during ticket sales openings. The system guarantees transactional integrity and prevents overselling under heavy load.

## Business Rules & Constraints
- **Inventory & Isolation:** A ticket can only be associated with one unique order (`order_id`). Overselling is strictly prohibited.
- **Temporary Reservation:** When a user selects a ticket, it must be locked in Redis with a 10-minute Time-To-Live (TTL). If the payment is not confirmed within 10 minutes, the lock expires, and the ticket status reverts to `AVAILABLE`.
- **Purchase Limits:** Maximum of 6 tickets sold per unique Identification Document (CPF/ID) per event.
- **Billing Requirements:** A valid billing address is mandatory to complete the transactional checkout.
- **Asynchronous Notifications:** A purchase confirmation event must trigger an asynchronous transactional email dispatch immediately after payment approval.
- **Pre-Sale Virtual Queue:** A waiting room/queue mechanism must manage traffic 30 minutes before the official ticket sales opening.

## State Machine (Ticket Status)
`AVAILABLE` -> `RESERVED` -> `PAID` / `EXPIRED` (Back to `AVAILABLE`)

## Tech Stack Decisions
- Backend: Java 21 & Spring Boot 3.x/4.x (Virtual Threads enabled)
- Security: Spring Security (Stateless JWT Authentication)
- Database: PostgreSQL (Read/Write tuning, Pessimistic/Optimistic locking where needed)
- Cache & Distributed Locks: Redis (Redisson or Lettuce for distributed ticket locks and TTL management)
- Infrastructure: Docker & Docker Compose