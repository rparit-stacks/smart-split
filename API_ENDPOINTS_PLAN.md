# SmartSplit API Endpoints - Complete Implementation Plan

## Overview
This document provides a comprehensive plan for all API endpoints required for the SmartSplit application. It details existing endpoints, missing endpoints, and the rationale behind each controller and endpoint.

---

## Table of Contents
1. [AuthController](#1-authcontroller) ✅
2. [UserController](#2-usercontroller) ⚠️
3. [GroupController](#3-groupcontroller) ✅
4. [ExpenseController](#4-expensecontroller) ❌ **MISSING - CRITICAL**
5. [SettlementController](#5-settlementcontroller) ❌ **MISSING - CRITICAL**
6. [CategoryController](#6-categorycontroller) ❌ **MISSING**
7. [AnalyticsController](#7-analyticscontroller) ❌ **MISSING**
8. [DashboardController](#8-dashboardcontroller) ❌ **MISSING**
9. [SearchController](#9-searchcontroller) ❌ **MISSING**
10. [ExportController](#10-exportcontroller) ❌ **MISSING**
11. [FileUploadController](#11-fileuploadcontroller) ❌ **MISSING**
12. [SettingsController](#12-settingscontroller) ❌ **MISSING**
13. [BalanceController](#13-balancecontroller) ❌ **MISSING**
14. [ActivityFeedController](#14-activityfeedcontroller) ❌ **MISSING**
15. [ReportController](#15-reportcontroller) ❌ **MISSING**

---

## 1. AuthController
**Status:** ✅ **COMPLETE**  
**Purpose:** Handles user authentication, registration, OTP verification, and password management.  
**Why Needed:** Core security and user onboarding functionality.

### Existing Endpoints

| Method | Endpoint | Description | Status | Why? |
|--------|----------|-------------|--------|------|
| `POST` | `/api/auth/register` | Register new user account | ✅ Complete | User onboarding - creates account with email/phone verification |
| `POST` | `/api/auth/login` | Initiate login (sends OTP) | ✅ Complete | First step of secure 2FA login flow |
| `POST` | `/api/auth/verify-login-otp` | Verify OTP and authenticate | ✅ Complete | Completes login, establishes session |
| `POST` | `/api/auth/logout` | Logout current user | ✅ Complete | Security - clears session |
| `POST` | `/api/auth/verify-email-otp` | Verify email address | ✅ Complete | Account verification during registration |
| `POST` | `/api/auth/verify-mobile-otp` | Verify phone number | ✅ Complete | Phone verification during registration |
| `POST` | `/api/auth/resend-email-otp` | Resend email OTP | ✅ Complete | User convenience - retry verification |
| `POST` | `/api/auth/resend-mobile-otp` | Resend mobile OTP | ✅ Complete | User convenience - retry verification |
| `POST` | `/api/auth/resend-login-otp` | Resend login OTP | ✅ Complete | User convenience - retry login |
| `POST` | `/api/auth/forgot-password` | Initiate password reset | ✅ Complete | Account recovery flow |
| `POST` | `/api/auth/reset-password` | Complete password reset | ✅ Complete | Account recovery - set new password |
| `POST` | `/api/auth/resend-forgot-password-otp` | Resend forgot password OTP | ✅ Complete | User convenience - retry password reset |

### Notes
- All authentication endpoints are properly implemented
- OTP-based authentication provides secure 2FA
- Session management is handled via Spring Security

---

## 2. UserController
**Status:** ⚠️ **NEEDS REFACTORING**  
**Purpose:** User profile management, user information retrieval, and personal data operations.  
**Why Needed:** Users need to manage their profiles, view other users, and access their personal information.

### Existing Endpoints

| Method | Endpoint | Description | Status | Why? | Issues |
|--------|----------|-------------|--------|------|--------|
| `GET` | `/api/users` | Get all users | ✅ Exists | Admin/explore functionality | Should be paginated |
| `GET` | `/api/users/get-user` | Get user by ID | ⚠️ Exists | View user profiles | **❌ Non-RESTful: Should be `/api/users/{id}`** |
| `POST` | `/api/users/create-user` | Create user | ⚠️ Exists | User creation | **❌ Should be in AuthController** |
| `PUT` | `/api/users/{id}` | Update user profile | ✅ Exists | Edit profile information | ✅ RESTful |
| `DELETE` | `/api/users/delete-user` | Delete user | ⚠️ Exists | Account deletion | **❌ Non-RESTful: Should be `/api/users/{id}`** |
| `GET` | `/api/users/get-user-groups` | Get user's groups | ⚠️ Exists | List user's groups | **❌ Non-RESTful: Should be `/api/users/{id}/groups`** |
| `GET` | `/api/users/get-user-balance-summary` | Get balance summary | ⚠️ Exists | Dashboard data | **❌ Non-RESTful: Should be `/api/users/{id}/balance-summary`** |
| `GET` | `/api/users/me` | Get current user | ✅ Exists | Get logged-in user info | ✅ RESTful |

### Missing Endpoints

| Method | Endpoint | Description | Why? |
|--------|----------|-------------|------|
| `GET` | `/api/users/{id}` | Get user by ID | **Standard REST endpoint** - View any user's profile |
| `DELETE` | `/api/users/{id}` | Delete user account | **Standard REST endpoint** - Account deletion |
| `GET` | `/api/users/{id}/groups` | Get user's groups | **Nested resource** - List all groups user belongs to |
| `GET` | `/api/users/{id}/expenses` | Get user's expenses | **Nested resource** - View all expenses user participated in |
| `GET` | `/api/users/{id}/balance-summary` | Get user's balance summary | **Nested resource** - Overall "owe/owed" across all groups |
| `GET` | `/api/users/{id}/settlements` | Get user's settlements | **Nested resource** - View payment history |
| `GET` | `/api/users/me/dashboard` | Get current user dashboard | **Convenience endpoint** - All dashboard data in one call |

### Refactoring Required
1. **Standardize REST paths**: Replace query parameters with path variables
2. **Remove duplicate endpoints**: `/api/users/create-user` should be removed (use `/api/auth/register`)
3. **Add nested resources**: Implement proper RESTful nested endpoints
4. **Add pagination**: For endpoints returning lists

---

## 3. GroupController
**Status:** ✅ **MOSTLY COMPLETE**  
**Purpose:** Group management - creating groups, managing members, and group operations.  
**Why Needed:** Core feature - users create groups to split expenses with friends/family.

### Existing Endpoints

| Method | Endpoint | Description | Status | Why? | Issues |
|--------|----------|-------------|--------|------|--------|
| `POST` | `/api/groups` | Create new group | ✅ Complete | Core feature - create expense groups | ✅ RESTful |
| `GET` | `/api/groups/{id}` | Get group details | ✅ Complete | View group info and members | ✅ RESTful |
| `PUT` | `/api/groups/{id}` | Update group | ✅ Complete | Edit group name, description, image | ✅ RESTful |
| `DELETE` | `/api/groups/{id}` | Delete group | ✅ Complete | Remove group (if no pending expenses) | ✅ RESTful |
| `POST` | `/api/groups/{id}/members` | Add member to group | ✅ Complete | Invite users to group | ✅ RESTful |
| `DELETE` | `/api/groups/{id}/members/{userId}` | Remove member | ✅ Complete | Remove user from group | ✅ RESTful |
| `POST` | `/api/groups/{id}/leave` | Leave group | ⚠️ Exists | User leaves voluntarily | **❌ Bug: Parameter mismatch (`groupId` vs `id`)** |

### Missing Endpoints

| Method | Endpoint | Description | Why? |
|--------|----------|-------------|------|
| `GET` | `/api/groups` | List all groups (for current user) | **List endpoint** - Show all groups user belongs to |
| `GET` | `/api/groups/{id}/expenses` | Get group expenses | **Nested resource** - List all expenses in group (activity feed) |
| `GET` | `/api/groups/{id}/balances` | Get group balances | **Nested resource** - Show who owes whom in this group |
| `GET` | `/api/groups/{id}/members` | Get group members | **Nested resource** - List all members with their details |
| `GET` | `/api/groups/{id}/settlements` | Get group settlements | **Nested resource** - Payment history within group |
| `GET` | `/api/groups/{id}/summary` | Get group summary | **Convenience endpoint** - Total expenses, balances, recent activity |
| `PUT` | `/api/groups/{id}/members/{userId}/role` | Update member role | **Future feature** - Admin/member roles |

### Issues to Fix
1. **Fix `/api/groups/{id}/leave`**: Parameter should be `@PathVariable UUID id` not `groupId`
2. **Add pagination**: For expenses and members lists
3. **Add filtering**: Filter expenses by date, category, user

---

## 4. ExpenseController
**Status:** ❌ **MISSING - CRITICAL**  
**Purpose:** Core business logic - creating, managing, and viewing expenses.  
**Why Needed:** This is the heart of the app - users need to add expenses and split them among group members.

### Required Endpoints

| Method | Endpoint | Description | Why? | Implementation Notes |
|--------|----------|-------------|------|---------------------|
| `POST` | `/api/expenses` | Create new expense | **Core feature** - Add expense and split among participants | Must handle: equal split, exact amounts, percentages |
| `GET` | `/api/expenses/{id}` | Get expense details | View expense with all participants and split details | Include paidBy, participants, category, group |
| `PUT` | `/api/expenses/{id}` | Update expense | Edit amount, title, description, or split | Recalculate balances if amount/split changes |
| `DELETE` | `/api/expenses/{id}` | Delete expense | Remove invalid/incorrect expenses | Recalculate all affected balances |
| `GET` | `/api/expenses` | List expenses (with filters) | Activity feed, search expenses | Support filters: group, user, category, date range, pagination |
| `GET` | `/api/expenses/recent` | Get recent expenses | Quick access to latest activity | Last 10-20 expenses across all groups |
| `GET` | `/api/expenses/group/{groupId}` | Get expenses by group | Group-specific expense list | Filtered by group ID, paginated |
| `GET` | `/api/expenses/user/{userId}` | Get expenses by user | User's expense history | All expenses user paid or participated in |
| `GET` | `/api/expenses/category/{categoryId}` | Get expenses by category | Category-based filtering | Filter expenses by category |
| `GET` | `/api/expenses/date-range` | Get expenses by date range | Time-based filtering | Query params: startDate, endDate |
| `PATCH` | `/api/expenses/{id}/mark-paid` | Mark expense as paid | Update participant payment status | Mark individual participant as paid |
| `GET` | `/api/expenses/{id}/participants` | Get expense participants | View who owes what for this expense | List all participants with amounts |
| `PATCH` | `/api/expenses/{id}/participants/{participantId}` | Update participant status | Mark participant as paid/unpaid | Update payment status |
| `PUT` | `/api/expenses/{id}/participants/{participantId}/amount` | Update participant share | Adjust individual share amount | Recalculate balances |
| `DELETE` | `/api/expenses/{id}/participants/{participantId}` | Remove participant | Remove user from expense split | Recalculate remaining shares |

### Required Endpoints

| Method | Endpoint | Description | Why? | Implementation Notes |
|--------|----------|-------------|------|---------------------|
| `POST` | `/api/expenses` | Create new expense | **Core feature** - Add expense and split among participants | Must handle: equal split, exact amounts, percentages |
| `GET` | `/api/expenses/{id}` | Get expense details | View expense with all participants and split details | Include paidBy, participants, category, group |
| `PUT` | `/api/expenses/{id}` | Update expense | Edit amount, title, description, or split | Recalculate balances if amount/split changes |
| `DELETE` | `/api/expenses/{id}` | Delete expense | Remove invalid/incorrect expenses | Recalculate all affected balances |
| `GET` | `/api/expenses` | List expenses (with filters) | Activity feed, search expenses | Support filters: group, user, category, date range |
| `GET` | `/api/expenses/recent` | Get recent expenses | Quick access to latest activity | Last 10-20 expenses across all groups |
| `GET` | `/api/expenses/group/{groupId}` | Get expenses by group | Group-specific expense list | Filtered by group ID |
| `GET` | `/api/expenses/user/{userId}` | Get expenses by user | User's expense history | All expenses user paid or participated in |
| `PATCH` | `/api/expenses/{id}/mark-paid` | Mark expense as paid | Update participant payment status | Mark individual participant as paid |

### Implementation Requirements
1. **Split Logic**: Support three split types:
   - **Equal**: Divide equally among selected participants
   - **Exact**: Each participant pays specific amount (must sum to total)
   - **Percentage**: Each participant pays percentage of total (must sum to 100%)
2. **Balance Calculation**: Automatically update balances when expense is created/updated/deleted
3. **Validation**: 
   - Verify all participants are group members
   - Verify split amounts sum correctly
   - Verify paidBy user is in group
4. **Transaction Management**: Use `@Transactional` for data consistency

### DTOs Required
- `ExpenseRequestDTO` ✅ (exists)
- `ExpenseResponseDTO` ✅ (exists)
- `ExpenseParticipantRequestDTO` ✅ (exists)
- `ExpenseParticipantResponseDTO` ✅ (exists)

### Service Layer
- Create `ExpenseService` with methods:
  - `createExpense(ExpenseRequestDTO)` - Creates expense + participants
  - `updateExpense(UUID, ExpenseRequestDTO)` - Updates and recalculates
  - `deleteExpense(UUID)` - Deletes and recalculates balances
  - `getExpenseById(UUID)` - Returns expense with participants
  - `getExpensesByGroup(UUID)` - List expenses in group
  - `getExpensesByUser(UUID)` - List user's expenses
  - `calculateSplit(ExpenseRequestDTO)` - Helper for split calculations

---

## 5. SettlementController
**Status:** ❌ **MISSING - CRITICAL**  
**Purpose:** Record payments between users to settle debts.  
**Why Needed:** When users pay each other (cash, Venmo, etc.), they need to record it to clear debts in the app.

### Required Endpoints

| Method | Endpoint | Description | Why? | Implementation Notes |
|--------|----------|-------------|------|---------------------|
| `POST` | `/api/settlements` | Record a settlement/payment | **Core feature** - User A pays User B | Updates balances between two users |
| `GET` | `/api/settlements/{id}` | Get settlement details | View payment receipt/history | Include payer, payee, amount, date, group |
| `PUT` | `/api/settlements/{id}` | Update settlement | Edit amount or description | Recalculate balances |
| `DELETE` | `/api/settlements/{id}` | Delete/undo settlement | Remove incorrect settlement | Revert balance changes |
| `GET` | `/api/settlements` | List settlements (with filters) | View payment history | Filter by group, user, date range |
| `GET` | `/api/settlements/group/{groupId}` | Get settlements by group | Group payment history | All settlements within a group |
| `GET` | `/api/settlements/user/{userId}` | Get settlements by user | User's payment history | All settlements user was involved in |
| `POST` | `/api/settlements/bulk` | Record multiple settlements | Batch payment recording | For complex settlement scenarios |

### Implementation Requirements
1. **Balance Update**: When settlement is created:
   - Decrease `payer`'s debt to `payee`
   - Increase `payee`'s debt to `payer` (or decrease if negative)
2. **Validation**:
   - Verify both users are in the same group
   - Verify settlement amount doesn't exceed debt
   - Verify users exist
3. **Transaction Management**: Use `@Transactional` for consistency
4. **Settlement Logic**: 
   - Can settle partial amounts
   - Should track net balance (A owes B $50, B pays A $30 = A owes B $20)

### DTOs Required
- `SettlementRequestDTO` ✅ (exists)
- `SettlementResponseDTO` ✅ (exists)

### Service Layer
- Create `SettlementService` with methods:
  - `createSettlement(SettlementRequestDTO)` - Creates settlement and updates balances
  - `updateSettlement(UUID, SettlementRequestDTO)` - Updates and recalculates
  - `deleteSettlement(UUID)` - Deletes and reverts balances
  - `getSettlementById(UUID)` - Returns settlement details
  - `getSettlementsByGroup(UUID)` - List settlements in group
  - `getSettlementsByUser(UUID)` - List user's settlements
  - `calculateBalanceAfterSettlement(User, User, BigDecimal)` - Helper for balance updates

---

## 6. CategoryController
**Status:** ❌ **MISSING**  
**Purpose:** Manage expense categories (Food, Travel, Utilities, etc.).  
**Why Needed:** Users need to categorize expenses for better organization and reporting.

### Required Endpoints

| Method | Endpoint | Description | Why? | Implementation Notes |
|--------|----------|-------------|------|---------------------|
| `GET` | `/api/categories` | List all categories | Populate dropdown in expense form | Return default + custom categories |
| `GET` | `/api/categories/{id}` | Get category details | View category info | Include name, description, color |
| `POST` | `/api/categories` | Create custom category | Allow users to create categories | User-specific or global (based on role) |
| `PUT` | `/api/categories/{id}` | Update category | Edit category details | Only if user owns it or is admin |
| `DELETE` | `/api/categories/{id}` | Delete category | Remove unused categories | Only if no expenses use it |
| `GET` | `/api/categories/popular` | Get popular categories | Show most-used categories | For quick selection |

### Implementation Requirements
1. **Default Categories**: Seed database with common categories:
   - Food & Dining
   - Travel
   - Utilities
   - Shopping
   - Entertainment
   - Healthcare
   - Education
   - Other
2. **Custom Categories**: Users can create their own (optional feature)
3. **Color Coding**: Each category should have a color for UI display
4. **Validation**: Prevent deletion if category is used in expenses

### DTOs Required
- `CategoryRequestDTO` ✅ (exists)
- `CategoryResponseDTO` ✅ (exists)

### Service Layer
- Create `CategoryService` with methods:
  - `getAllCategories()` - Returns all categories
  - `getCategoryById(UUID)` - Returns category details
  - `createCategory(CategoryRequestDTO)` - Creates new category
  - `updateCategory(UUID, CategoryRequestDTO)` - Updates category
  - `deleteCategory(UUID)` - Deletes if not in use
  - `getPopularCategories()` - Returns most-used categories

---

## 7. AnalyticsController
**Status:** ❌ **MISSING**  
**Purpose:** Advanced analytics, statistics, and insights for expenses and spending patterns.  
**Why Needed:** Users want to understand their spending habits, trends, and get insights from their expense data.

### Required Endpoints

| Method | Endpoint | Description | Why? | Returns |
|--------|----------|-------------|------|---------|
| `GET` | `/api/analytics/overview` | Get analytics overview | Dashboard insights | Total spent, categories breakdown, trends |
| `GET` | `/api/analytics/spending-by-category` | Spending by category | Category analysis | Category-wise spending totals and percentages |
| `GET` | `/api/analytics/spending-by-group` | Spending by group | Group analysis | Group-wise spending totals |
| `GET` | `/api/analytics/spending-trends` | Spending trends over time | Time-based analysis | Monthly/weekly/daily spending trends |
| `GET` | `/api/analytics/top-expenses` | Top expenses | Identify largest expenses | Highest amount expenses |
| `GET` | `/api/analytics/top-categories` | Top spending categories | Most used categories | Categories sorted by total spending |
| `GET` | `/api/analytics/monthly-summary` | Monthly spending summary | Monthly reports | Total spent, count, average per month |
| `GET` | `/api/analytics/yearly-summary` | Yearly spending summary | Annual reports | Yearly totals and trends |
| `GET` | `/api/analytics/group/{groupId}/analytics` | Group analytics | Group-specific insights | Spending patterns within group |
| `GET` | `/api/analytics/user/{userId}/analytics` | User analytics | User-specific insights | Individual spending patterns |
| `GET` | `/api/analytics/balance-trends` | Balance trends over time | Track debt/credit trends | How balances change over time |
| `GET` | `/api/analytics/payment-frequency` | Payment frequency analysis | Settlement patterns | How often users settle up |
| `GET` | `/api/analytics/comparison` | Compare periods | Period comparison | Compare spending between time periods |

### Implementation Notes
1. **Aggregation Queries**: Use database aggregation for performance
2. **Caching**: Cache analytics data for better performance
3. **Date Ranges**: Support custom date ranges for all analytics
4. **Grouping**: Support grouping by day, week, month, year

---

## 8. DashboardController
**Status:** ❌ **MISSING**  
**Purpose:** Aggregate dashboard data for quick access.  
**Why Needed:** Frontend needs consolidated data for dashboard view without multiple API calls.

### Required Endpoints

| Method | Endpoint | Description | Why? | Returns |
|--------|----------|-------------|------|---------|
| `GET` | `/api/dashboard` | Get user dashboard data | **Convenience endpoint** - All dashboard data in one call | Groups, balances, recent expenses, settlements, quick stats |
| `GET` | `/api/dashboard/balances` | Get balance summary | Quick balance overview | Total owe, total owed, by group, net balance |
| `GET` | `/api/dashboard/recent-activity` | Get recent activity | Activity feed | Recent expenses and settlements (last 10-20) |
| `GET` | `/api/dashboard/statistics` | Get user statistics | Analytics/insights | Total spent, categories breakdown, expense count |
| `GET` | `/api/dashboard/groups-summary` | Get groups summary | Quick groups overview | List of groups with member count, recent activity |
| `GET` | `/api/dashboard/upcoming-settlements` | Get upcoming settlements | Reminders | Debts that need to be settled |
| `GET` | `/api/dashboard/quick-stats` | Get quick statistics | Dashboard widgets | Total expenses, total groups, active balances |

### Implementation Notes
1. **Aggregation**: Combines data from multiple services (User, Group, Expense, Settlement)
2. **Performance**: Consider caching for frequently accessed data
3. **Optimization**: Use single query with joins where possible
4. **Pagination**: For activity feeds

---

## Summary

### ✅ Complete Controllers (3)
1. **AuthController** - Fully implemented ✅
2. **GroupController** - Mostly complete (needs minor fixes) ✅
3. **UserController** - Exists but needs refactoring ⚠️

### ❌ Missing Controllers (12)
**CRITICAL (Must Have):**
1. **ExpenseController** - **CRITICAL** - Core business logic
2. **SettlementController** - **CRITICAL** - Payment/settlement tracking

**IMPORTANT (Should Have):**
3. **CategoryController** - Expense categorization
4. **DashboardController** - Aggregated dashboard data
5. **AnalyticsController** - Spending analytics and insights
6. **BalanceController** - Detailed balance calculations
7. **ActivityFeedController** - Comprehensive activity feeds

**NICE TO HAVE (Optional but Recommended):**
8. **SearchController** - Global search functionality
9. **ExportController** - Data export (CSV, PDF, Excel)
10. **FileUploadController** - File/image uploads
11. **SettingsController** - User preferences
12. **ReportController** - Comprehensive financial reports

### 🔧 Refactoring Required
1. **UserController**: Standardize REST paths, remove non-RESTful endpoints
2. **GroupController**: Fix `/leave` endpoint parameter bug
3. **Add pagination**: For all list endpoints
4. **Add filtering**: For expenses, settlements, groups

### 📊 Priority Order

**PHASE 1 - CRITICAL (Must Implement First):**
1. **ExpenseController** - Core business logic, app cannot function without it
2. **SettlementController** - Core feature for settling debts
3. **UserController refactoring** - Fix REST standards, code quality

**PHASE 2 - IMPORTANT (Core Functionality):**
4. **CategoryController** - Required for expense categorization
5. **DashboardController** - Essential for user experience
6. **BalanceController** - Detailed balance calculations
7. **ActivityFeedController** - User activity tracking

**PHASE 3 - ENHANCEMENT (Better UX):**
8. **AnalyticsController** - Spending insights and trends
9. **SearchController** - Find expenses, groups, users quickly
10. **ExportController** - Data export for accounting/taxes

**PHASE 4 - OPTIONAL (Nice to Have):**
11. **FileUploadController** - Receipts, profile pictures
12. **SettingsController** - User preferences
13. **ReportController** - Comprehensive reports

---

## RESTful Design Principles Applied

### ✅ Good Practices
- Use path variables for resource IDs: `/api/users/{id}`
- Use HTTP methods correctly: GET (read), POST (create), PUT (update), DELETE (remove)
- Nested resources: `/api/groups/{id}/expenses`
- Consistent naming: plural nouns for collections

### ❌ Issues to Fix
- Query parameters for IDs: `/api/users/get-user?id=...` → `/api/users/{id}`
- Non-standard paths: `/api/users/create-user` → `/api/users` (POST)
- Missing list endpoints: Add `GET /api/groups` for listing
- Inconsistent nested resources: Some use query params, should use path variables

---

## Next Steps

1. **Create ExpenseController** with all CRUD operations
2. **Create SettlementController** with balance management
3. **Create CategoryController** with default categories
4. **Refactor UserController** to follow REST standards
5. **Fix GroupController** `/leave` endpoint
6. **Add missing nested endpoints** to existing controllers
7. **Add pagination and filtering** to list endpoints
8. **Create service layers** for new controllers
9. **Add comprehensive error handling** and validation
10. **Write integration tests** for all endpoints

---

## Notes

- All endpoints should return consistent error responses using `ErrorDto`
- All endpoints should validate input using DTOs
- All endpoints should handle authentication (except public auth endpoints)
- Consider adding rate limiting for sensitive endpoints
- Add API versioning if needed: `/api/v1/...`
- Document all endpoints with Swagger/OpenAPI

---

---

## Endpoint Summary Statistics

### Total Endpoints by Controller

| Controller | Existing | Missing | Total | Status |
|------------|----------|---------|-------|--------|
| **AuthController** | 12 | 0 | 12 | ✅ Complete |
| **UserController** | 8 | 6 | 14 | ⚠️ Needs Refactoring |
| **GroupController** | 7 | 6 | 13 | ✅ Mostly Complete |
| **ExpenseController** | 0 | 13 | 13 | ❌ Missing |
| **SettlementController** | 0 | 8 | 8 | ❌ Missing |
| **CategoryController** | 0 | 6 | 6 | ❌ Missing |
| **AnalyticsController** | 0 | 13 | 13 | ❌ Missing |
| **DashboardController** | 0 | 7 | 7 | ❌ Missing |
| **SearchController** | 0 | 6 | 6 | ❌ Missing |
| **ExportController** | 0 | 7 | 7 | ❌ Missing |
| **FileUploadController** | 0 | 6 | 6 | ❌ Missing |
| **SettingsController** | 0 | 10 | 10 | ❌ Missing |
| **BalanceController** | 0 | 7 | 7 | ❌ Missing |
| **ActivityFeedController** | 0 | 5 | 5 | ❌ Missing |
| **ReportController** | 0 | 8 | 8 | ❌ Missing |
| **TOTAL** | **27** | **110** | **137** | |

### Endpoint Breakdown by HTTP Method

| Method | Count | Purpose |
|--------|-------|---------|
| `GET` | ~75 | Read/Retrieve operations |
| `POST` | ~35 | Create operations |
| `PUT` | ~15 | Full update operations |
| `PATCH` | ~8 | Partial update operations |
| `DELETE` | ~10 | Delete operations |

---

## Complete Implementation Checklist

### Phase 1: Critical Controllers (Must Have)
- [ ] **ExpenseController** - 13 endpoints
  - [ ] Create expense with split logic
  - [ ] CRUD operations
  - [ ] Participant management
  - [ ] Filtering and pagination
- [ ] **SettlementController** - 8 endpoints
  - [ ] Settlement creation with balance updates
  - [ ] CRUD operations
  - [ ] Balance recalculation logic
- [ ] **UserController Refactoring**
  - [ ] Fix REST paths
  - [ ] Remove non-RESTful endpoints
  - [ ] Add nested resources

### Phase 2: Important Controllers
- [ ] **CategoryController** - 6 endpoints
- [ ] **DashboardController** - 7 endpoints
- [ ] **BalanceController** - 7 endpoints
- [ ] **ActivityFeedController** - 5 endpoints

### Phase 3: Enhancement Controllers
- [ ] **AnalyticsController** - 13 endpoints
- [ ] **SearchController** - 6 endpoints
- [ ] **ExportController** - 7 endpoints

### Phase 4: Optional Controllers
- [ ] **FileUploadController** - 6 endpoints
- [ ] **SettingsController** - 10 endpoints
- [ ] **ReportController** - 8 endpoints

### Cross-Cutting Concerns
- [ ] Add pagination to all list endpoints
- [ ] Add filtering support (date, category, group, user)
- [ ] Implement error handling with ErrorDto
- [ ] Add input validation for all DTOs
- [ ] Add authentication/authorization checks
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Add rate limiting for sensitive endpoints
- [ ] Add caching for frequently accessed data
- [ ] Add logging and monitoring
- [ ] Write integration tests for all endpoints

---

## Additional Considerations

### Performance Optimization
1. **Database Indexing**: Add indexes on frequently queried fields
   - User email, phone
   - Expense dates, group_id, user_id
   - Settlement dates, payer_id, payee_id
2. **Query Optimization**: Use eager/lazy loading appropriately
3. **Caching Strategy**: Cache analytics, dashboard data, categories
4. **Pagination**: Implement cursor-based pagination for large datasets

### Security
1. **Input Validation**: Validate all inputs using DTOs and Bean Validation
2. **Authorization**: Check user permissions for all operations
3. **Rate Limiting**: Prevent abuse of endpoints
4. **SQL Injection**: Use parameterized queries (JPA handles this)
5. **XSS Protection**: Sanitize user inputs

### Data Integrity
1. **Transactions**: Use `@Transactional` for multi-step operations
2. **Balance Consistency**: Ensure balances are always consistent
3. **Cascade Deletes**: Handle cascading deletes appropriately
4. **Soft Deletes**: Consider soft deletes for important entities

### API Design
1. **Versioning**: Consider `/api/v1/` prefix for future versions
2. **Consistent Responses**: Use consistent response format
3. **Error Codes**: Define standard error codes
4. **Documentation**: Complete Swagger/OpenAPI documentation

---

**Last Updated:** 2025-01-XX  
**Status:** Planning Phase - Ready for Implementation  
**Total Endpoints Planned:** 137  
**Existing Endpoints:** 27  
**Missing Endpoints:** 110

