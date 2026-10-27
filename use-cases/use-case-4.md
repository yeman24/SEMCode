
# Use Case 4: Salary Report by Role

## Goal in Context
As an HR advisor I want to produce a report on the salary of employees of a given role so that I can support financial reporting.

## Scope
HR System

## Level
User goal

## Preconditions
The database is available.

## Success Condition
A salary report is displayed for the selected role.

## Failed Condition
The report cannot be generated.

## Primary Actor
HR Advisor

## Trigger
HR advisor requests a salary report by role.

## Main Success Scenario
1. HR advisor selects salary report by role.
2. System asks for a job title.
3. HR advisor enters a job title.
4. System searches the employee database.
5. System displays employee names and salaries.

## Extensions
- Invalid job title: Display an error.
- No matching employees: Display a message.
- Database unavailable: Display an error.

## Sub-variations
Different job titles can be selected.

## Schedule
To be delivered during development.
