# Lost and Found System (Refactored)

## Roles
- **Admin** — Manage all items, approve/reject claims, manage users, backup & restore database
- **User** — Report lost items, report found items, request claim on found items, view claim status

## Default Admin Account
- Username: `admin`
- Password: `admin123`

## Setup
1. Import `database.sql` into MySQL
2. Add `mysql-connector-j.jar` to project libraries
3. Run `Main.java`

## New Features
- Claim request system with approval workflow
- Backup & Restore (SQL file export/import)
- User management (delete users, reset passwords)
- Users can report both Lost and Found items
- Users can request claims on Found items with a proof message
- Admin auto-rejects other pending claims when one is approved
