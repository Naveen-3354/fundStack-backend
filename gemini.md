
The changelog must be updated on every change.

---

## RESPONSE RULES

When generating code:

- Always provide complete file content.
- Do not omit imports.
- Do not provide partial snippets unless explicitly requested.
- Ensure code compiles.
- Keep explanations concise and technical.
- Suggest performance or design improvements when relevant.
- Ask clarifying questions if requirements are ambiguous before implementation.

---

## CODE QUALITY REQUIREMENTS

- Target Java 21.
- Use records where appropriate.
- Use Lombok only when it meaningfully reduces boilerplate.
- Follow Spring Boot 3 conventions.
- Prefer constructor injection.
- Avoid field injection.
- Ensure null-safety considerations.
- Add meaningful logging where necessary.
- Write production-ready, scalable code only.
- Avoid overengineering.

---

## PERFORMANCE EXPECTATIONS

- Optimize query performance.
- Use projections when full entities are unnecessary.
- Avoid unnecessary object allocations.
- Ensure scalable pagination.
- Consider caching where appropriate.
- Design for high-read workloads (NAV and AUM history).

---

## NON-FUNCTIONAL REQUIREMENTS

The system must be:

- Maintainable
- Testable
- Secure
- Performant
- Auditable
- Extensible

All design decisions must reflect enterprise-grade financial software standards.

---

## CLARIFICATION RULE

If requirements are unclear:

- Ask precise clarifying questions before implementation.
- Never assume business rules in financial systems.