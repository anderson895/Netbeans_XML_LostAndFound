# ASCOT Lost and Found System

A Java Swing-based Lost and Found management system for Aurora State College of Technology (ASCOT).

## Changes Applied (Revision)

### 1. Student ID replaces Username
- Login and Register now use **Student ID** (format: `21-01-1155`)
- Database column changed from `username` to `student_id`

### 2. Full Name Format
- Register form now has separate fields: **Last Name**, **Given Name**, **Middle Name**
- Stored as: `Last Name, Given Name, Middle Name` (e.g., `De Asis, Diana Kyla, I.`)

### 3. Calendar Drop-down
- Date field now uses **3 dropdown combos** (Year, Month, Day) instead of a text field

### 4. Location Drop-down
- Location is now a **dropdown** with ASCOT campus locations:
  - ASCOT ENTRANCE, ASCOT EXIT, ASCOT HOSTEL, ICTC, Engineering Building, General Education Buildings, Senator Edgardo Angara Hall

### 5. Admin-Only Actions
- Only admin can: **edit, update, delete** items, **backup & restore** (full DB + single-entry via Recycle Bin), **accept/reject** claims, **manage users**, **set verification questions**

### 6. User Actions
- Users can: **create/report/submit** entries, **request claims** (with verification), **browse found items**
- Edit/delete removed from user dashboard

### 7. Analytics Dashboard (Admin)
- New **Analytics tab** with stat cards and bar chart showing Lost, Found, Claimed, and Total item counts

### 8. Recycle Bin (Admin)
- Deleted items go to a **Recycle Bin** (soft delete)
- Admin can **restore individual entries** or permanently delete

### 9. Claim Verification
- Admin can set a **verification question** on Found items
- Users must answer the verification question when claiming

### 10. Sidebar Layout
- Both Admin and User dashboards use a **dark sidebar** with navigation
- **Logout button at the bottom** of the sidebar
- **Search Reference** link opens browser

### 11. Improved Design
- Custom color palette (Navy, Teal, accent colors)
- Styled headers, cards, buttons, and table formatting
- Consistent professional look across all forms

## Setup
1. Run `database.sql` in MySQL to create the database
2. Open in NetBeans, add `mysql-connector-j.jar` to Libraries
3. Run `Main.java`

## Default Accounts
- Admin: Student ID `21-00-0001`, Password `admin123`
- User: Student ID `21-01-1155`, Password `pass123`
