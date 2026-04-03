# ChatApp — Java JDBC + Servlet + JSP + PostgreSQL

A full-stack chat application with user and admin roles, wall posts, group chat, comments,
and admin moderation tools.

---

## Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Backend    | Java 17, Jakarta Servlet 6, JDBC    |
| Frontend   | JSP, JSTL, Vanilla JS (polling)     |
| Database   | PostgreSQL 14+                      |
| Security   | BCrypt (jBCrypt), Session auth      |
| Build      | Maven 3.8+                          |
| Server     | Apache Tomcat 10.1+                 |

---

## Project Structure

```
ChatApp/
├── sql/
│   └── schema.sql                   ← Run this first in PostgreSQL
├── pom.xml
└── src/main/
    ├── java/com/chatapp/
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Post.java
    │   │   ├── Comment.java
    │   │   ├── Group.java
    │   │   └── GroupMessage.java
    │   ├── dao/
    │   │   ├── UserDAO.java          ← signup, login, BCrypt auth
    │   │   ├── PostDAO.java          ← CRUD, hide/unhide, share
    │   │   ├── CommentDAO.java       ← CRUD, hide/unhide
    │   │   └── GroupDAO.java         ← groups, members, chat msgs
    │   ├── servlet/
    │   │   ├── user/
    │   │   │   ├── SignupServlet.java
    │   │   │   ├── LoginServlet.java
    │   │   │   ├── LogoutServlet.java
    │   │   │   ├── FeedServlet.java
    │   │   │   ├── CommentServlet.java
    │   │   │   ├── GroupServlet.java
    │   │   │   └── ChatPollServlet.java   ← AJAX polling
    │   │   └── admin/
    │   │       ├── AdminLoginServlet.java
    │   │       ├── AdminSignupServlet.java
    │   │       ├── AdminLogoutServlet.java
    │   │       ├── AdminDashboardServlet.java
    │   │       ├── AdminPostServlet.java
    │   │       ├── AdminGroupServlet.java
    │   │       └── AdminUserServlet.java
    │   ├── filter/
    │   │   └── AuthFilter.java           ← Protects all routes
    │   └── util/
    │       ├── DBUtil.java               ← JDBC connection
    │       └── ValidationUtil.java       ← Email + username validation
    └── webapp/
        ├── css/style.css
        ├── index.jsp                     ← Redirects to /login
        ├── WEB-INF/web.xml
        └── jsp/
            ├── common/
            │   ├── navbar.jsp
            │   ├── login.jsp
            │   └── signup.jsp
            ├── user/
            │   ├── feed.jsp              ← Wall posts + comments
            │   ├── groups.jsp            ← Discover & join groups
            │   └── group.jsp             ← Group chat + group posts
            └── admin/
                ├── login.jsp
                ├── signup.jsp
                ├── dashboard.jsp
                ├── users.jsp
                ├── groups.jsp
                └── group.jsp             ← Full moderation panel
```

---

## Quick Setup

### 1. PostgreSQL

```sql
-- Create database
CREATE DATABASE chatapp;
\c chatapp

-- Run schema
\i /path/to/ChatApp/sql/schema.sql
```

### 2. Configure DB connection

Edit `src/main/java/com/chatapp/util/DBUtil.java`:

```java
private static final String DB_URL  = "jdbc:postgresql://localhost:5432/chatapp";
private static final String DB_USER = "postgres";
private static final String DB_PASS = "yourpassword";   // ← change this
```

### 3. Build

```bash
cd ChatApp
mvn clean package
```

This produces `target/ChatApp.war`.

### 4. Deploy to Tomcat 10.1+

```bash
cp target/ChatApp.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
```

Open: **http://localhost:8080/ChatApp**

---

## URL Routes

### User Routes

| Method | URL              | Description                        |
|--------|------------------|------------------------------------|
| GET    | `/login`         | Login page                         |
| POST   | `/login`         | Authenticate user                  |
| GET    | `/signup`        | Register page                      |
| POST   | `/signup`        | Create user account                |
| GET    | `/logout`        | Invalidate session                 |
| GET    | `/feed`          | Wall feed (posts + comments)       |
| POST   | `/feed`          | Create a wall post                 |
| POST   | `/comment`       | Add comment to a post              |
| GET    | `/groups`        | List all groups                    |
| GET    | `/groups?id=N`   | Open a specific group              |
| POST   | `/groups`        | Join / leave / chat / post         |
| GET    | `/chat/poll`     | AJAX: get new chat messages        |

### Admin Routes

| Method | URL                     | Description                             |
|--------|-------------------------|-----------------------------------------|
| GET    | `/admin/login`          | Admin login page                        |
| POST   | `/admin/login`          | Authenticate admin                      |
| GET    | `/admin/signup`         | Admin registration (requires secret key)|
| POST   | `/admin/signup`         | Create admin account                    |
| GET    | `/admin/logout`         | Invalidate admin session                |
| GET    | `/admin/dashboard`      | Overview + stats                        |
| GET    | `/admin/users`          | All users list                          |
| POST   | `/admin/users`          | Activate / deactivate user              |
| GET    | `/admin/groups`         | All groups list                         |
| POST   | `/admin/groups`         | Create group / manage members / hide msg|
| GET    | `/admin/groups?id=N`    | Full group moderation panel             |
| POST   | `/admin/posts`          | Hide / unhide / delete post or comment  |

---

## Features

### User
- ✅ Sign up with unique username + email (validated)
- ✅ Sign in / Sign out (session-based)
- ✅ Create wall posts (visible to all users)
- ✅ Comment on any post (multiple comments per post)
- ✅ Join and leave groups
- ✅ Real-time group chat (2-second AJAX polling)
- ✅ Post inside groups
- ✅ Comment on group posts
- ✅ Only see non-hidden posts and comments
- ✅ Only see posts shared with them (selective sharing)

### Admin
- ✅ Separate admin login with registration key (`CHATAPP_ADMIN_2024`)
- ✅ Dashboard with stats (users, posts, groups)
- ✅ Post anywhere (wall or any group)
- ✅ **Hide / Unhide** any post
- ✅ **Hide / Unhide** any comment
- ✅ **Hide / Unhide** any group chat message
- ✅ Delete posts and comments
- ✅ Activate / deactivate user accounts
- ✅ Create groups and manage membership
- ✅ Add or remove members from any group
- ✅ **Selective post sharing** — share a post to specific users only (others can't see it)
- ✅ Full group moderation panel with chat history

---

## Real-Time Chat

Chat uses **AJAX polling** (not WebSocket) via `GET /chat/poll?groupId=N&afterId=M`.

- Polls every **2 seconds**
- Returns only new messages (after the last seen ID)
- No full page reload — messages append smoothly
- Admin sees hidden messages (marked with opacity)
- Regular users only receive non-hidden messages

For production, replace polling with **WebSocket** (`@ServerEndpoint`) or **SSE** for lower latency.

---

## Selective Post Sharing (Slack-style)

When an admin posts in a group and selects specific members in the "Share only with"
dropdown, a row is inserted into `post_shares` for each selected user.

The SQL logic in `PostDAO.getGroupPosts()` (user variant):
```sql
WHERE p.group_id = ?
  AND p.is_hidden = FALSE
  AND (
    NOT EXISTS (SELECT 1 FROM post_shares WHERE post_id = p.id)   -- public post
    OR EXISTS  (SELECT 1 FROM post_shares WHERE post_id = p.id AND user_id = ?)  -- shared with me
  )
```

This mirrors how Slack channels work: posts are visible to all unless specifically
targeted — in which case only the targeted users see them.

---

## Security

| Concern            | Implementation                          |
|--------------------|-----------------------------------------|
| Password storage   | BCrypt with cost factor 12              |
| SQL injection      | All queries use `PreparedStatement`     |
| Session fixation   | `session.invalidate()` on logout        |
| Unauthorized access| `AuthFilter` on every request           |
| Direct JSP access  | Blocked via `web.xml` security-constraint|
| Admin registration | Protected by secret key                 |

---

## Validation Rules

| Field    | Rule                                             |
|----------|--------------------------------------------------|
| Username | 3–50 chars, `[a-zA-Z0-9_]` only, must be unique |
| Email    | RFC-compliant via Apache Commons Validator, unique|
| Password | Minimum 6 characters                             |

---

## Changing the Admin Secret Key

In `AdminSignupServlet.java`, line:
```java
if (!"CHATAPP_ADMIN_2024".equals(secret)) {
```
Change `CHATAPP_ADMIN_2024` to your own secret. In production, store this in a
properties file or environment variable.

---

## Dependencies (Maven)

| Library                    | Purpose                |
|----------------------------|------------------------|
| `jakarta.servlet-api`      | Servlet 6 API          |
| `jakarta.servlet.jsp-api`  | JSP 3.1 API            |
| `jakarta.servlet.jsp.jstl` | JSTL tags              |
| `postgresql`               | JDBC driver            |
| `jbcrypt`                  | BCrypt password hashing|
| `gson`                     | JSON for AJAX responses|
| `commons-validator`        | Email validation       |
