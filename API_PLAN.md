# SmartSplit API Implementation Plan

This document outlines the comprehensive plan for the API architecture of SmartSplit. It details existing endpoints that need refinement and new controllers required to fully support the application's functionality (Splitting expenses, managing groups, settlements, etc.).

## 1. AuthController
**Status:** ✅ Mostly Complete
**Purpose:** Handles user authentication, registration, and security.

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register new user | Onboarding new users. |
| `POST` | `/api/auth/login` | Login (Initiate OTP) | First step of secure login flow. |
| `POST` | `/api/auth/verify-login-otp` | Verify OTP & Issue Token | Completes login; issues session/JWT. |
| `POST` | `/api/auth/logout` | Logout | Security; clear session. |
| `POST` | `/api/auth/verify-email-otp` | Verify Email | Account verification. |
| `POST` | `/api/auth/forgot-password` | Initiate Password Reset | Account recovery. |
| `POST` | `/api/auth/reset-password` | Complete Password Reset | Account recovery. |

---

## 2. UserController
**Status:** ⚠️ Needs Refactoring (Standardize REST paths)
**Purpose:** User profile management and personal dashboard.

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me` | Get current logged-in user | Frontend needs to know who is logged in. |
| `GET` | `/api/users/{id}` | Get user profile | View friend profiles. *Refactor from query param.* |
| `PUT` | `/api/users/{id}` | Update profile | Edit name, phone, avatar. |
| `GET` | `/api/users/{id}/groups` | Get user's groups | Show list of groups on dashboard. |
| `GET` | `/api/users/{id}/friends` | Get user's friends | (Optional) If we implement friend system outside groups. |
| `GET` | `/api/users/dashboard` | Get overall balance summary | **Crucial:** Shows "You owe $X" and "You are owed $Y" across all groups. |

---

## 3. GroupController
**Status:** ✅ Good Shape
**Purpose:** Group management.

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/groups` | Create new group | Core feature. |
| `GET` | `/api/groups/{id}` | Get group details | View group header, info. |
| `PUT` | `/api/groups/{id}` | Update group | Edit name, description, cover photo. |
| `DELETE` | `/api/groups/{id}` | Delete group | Cleanup. |
| `POST` | `/api/groups/{id}/members` | Add member | Grow the group. |
| `DELETE` | `/api/groups/{id}/members/{userId}` | Remove member | Manage membership. |
| `POST` | `/api/groups/{id}/leave` | Leave group | User choice to exit. |
| `GET` | `/api/groups/{id}/expenses` | Get group expenses | **New:** List all transactions in this group (feed). |
| `GET` | `/api/groups/{id}/balances` | Get group balances | **New:** Show who owes whom within this specific group. |

---

## 4. ExpenseController (New)
**Status:** ❌ Missing
**Purpose:** Managing the core business entity: Expenses.

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/expenses` | Add new expense | The main action of the app. Needs to handle splitting logic (equal, exact, percent). |
| `GET` | `/api/expenses/{id}` | Get expense details | View who paid and how it was split. |
| `PUT` | `/api/expenses/{id}` | Edit expense | Mistakes happen; users need to fix amounts or splits. |
| `DELETE` | `/api/expenses/{id}` | Delete expense | Remove invalid entries. |
| `GET` | `/api/expenses/recent` | Get recent activity | Global activity feed for the user. |

**Implementation Notes:**
- Needs complex logic to calculate `ExpenseParticipant` shares.
- Should update group/user balances upon creation/update/deletion.

---

## 5. SettlementController (New)
**Status:** ❌ Missing
**Purpose:** Recording payments between users ("Settling Up").

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/settlements` | Record a payment | When User A pays User B cash/Venmo, this clears the debt in the app. |
| `GET` | `/api/settlements/{id}` | Get settlement details | View receipt. |
| `DELETE` | `/api/settlements/{id}` | Undo settlement | If recorded by mistake. |

---

## 6. CategoryController (New)
**Status:** ❌ Missing
**Purpose:** Metadata for expenses.

| Method | Endpoint | Description | Why? |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/categories` | List all categories | Populate "Category" dropdown in Add Expense form. |
| `POST` | `/api/categories` | Add category | (Admin or User) Allow custom categories. |

---

## Summary of Next Steps

1.  **Refactor `UserController`**: Fix URL patterns to be standard REST (e.g., `/api/users/{id}` instead of `?id=`).
2.  **Create `CategoryController`**: Simple CRUD, needed for Expenses.
3.  **Create `ExpenseController`**: The heaviest logic. Needs to handle:
    - Input DTO with total amount and split details.
    - creating `Expense` entity.
    - creating multiple `ExpenseParticipant` entities.
4.  **Create `SettlementController`**: Logic to "zero out" debts between two users.
