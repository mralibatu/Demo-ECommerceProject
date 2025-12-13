# TeamCity Simple Demo Project

A simple and stable demo project showcasing TeamCity CI/CD capabilities without complex dependencies.

## 🎯 Project Overview

This is a minimalistic **Product Management System** built with:
- **Backend**: Spring Boot with H2 database
- **Frontend**: Vanilla JavaScript with Bootstrap
- **Build Tool**: Maven
- **CI/CD**: TeamCity (3 build configurations)
- **Container**: Docker

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Docker (optional)

### Run Locally
```bash
# Clone the repository
git clone <your-repo-url>
cd simple-teamcity-demo

# Run the application
mvn spring-boot:run

# Access the application
open http://localhost:8080
```

### API Endpoints
```
GET    /api/products           # List all products
POST   /api/products           # Create product
GET    /api/products/{id}      # Get product by ID
PUT    /api/products/{id}      # Update product
DELETE /api/products/{id}      # Delete product
GET    /api/products/search    # Search products
GET    /api/products/stats     # Get statistics
```

### Health Check
```
GET /actuator/health          # Application health status
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Generate coverage report
mvn test jacoco:report

# Run quality checks
mvn checkstyle:check
```

## 🐳 Docker

```bash
# Build and run with Docker Compose
docker-compose up --build

# Or build manually
mvn package
docker build -t teamcity-demo .
docker run -p 8080:8080 teamcity-demo
```

## 🔧 TeamCity Configuration

This project includes **3 TeamCity build configurations**:

### 1. Build & Test
- Compiles the code
- Runs unit tests
- Generates test reports

### 2. Quality Check
- Code coverage analysis (JaCoCo)
- Code style check (Checkstyle)
- Generates quality reports

### 3. Package & Docker
- Creates JAR file
- Builds Docker image
- Publishes artifacts

### Build Chain Flow
```
[Build & Test] → [Quality Check] → [Package & Docker]
```

## 📊 Features Demonstrated

### TeamCity Features
- ✅ **VCS Integration** - Automatic builds on commit
- ✅ **Build Dependencies** - Sequential build chain
- ✅ **Artifact Management** - JAR files and reports
- ✅ **Test Integration** - Unit test execution
- ✅ **Code Coverage** - JaCoCo integration
- ✅ **Quality Gates** - Checkstyle validation
- ✅ **Docker Integration** - Container builds
- ✅ **Build Triggers** - VCS change detection

### Application Features
- ✅ **CRUD Operations** - Create, read, update, delete products
- ✅ **Search Functionality** - Find products by name
- ✅ **Statistics Dashboard** - Product count and total value
- ✅ **Responsive UI** - Mobile-friendly interface
- ✅ **REST API** - JSON-based API endpoints
- ✅ **Health Monitoring** - Application health checks

## 🎬 Demo Scenarios

### 1. **Basic CI/CD Flow**
1. Make a code change
2. Commit to repository
3. Watch TeamCity automatically trigger builds
4. See all 3 build configurations execute in sequence
5. Review artifacts and reports

### 2. **Test Failure Simulation**
1. Introduce a test failure
2. Commit the change
3. Observe build failure in TeamCity
4. Fix the issue
5. Watch builds turn green

### 3. **Quality Gate Demo**
1. Introduce code style violations
2. See quality check build fail
3. Fix style issues
4. See pipeline complete successfully

## 📁 Project Structure

```
teamcity-simple-demo/
├── src/
│   ├── main/java/com/demo/
│   │   ├── DemoApplication.java       # Main application
│   │   ├── Product.java               # Product entity
│   │   ├── ProductRepository.java     # Data access
│   │   ├── ProductService.java        # Business logic
│   │   └── ProductController.java     # REST endpoints
│   ├── main/resources/
│   │   ├── application.yml            # Configuration
│   │   ├── data.sql                   # Sample data
│   │   └── static/                    # Frontend files
│   └── test/java/com/demo/
│       ├── ProductServiceTest.java    # Unit tests
│       ├── ProductControllerTest.java # Controller tests
│       └── DemoApplicationTest.java   # Integration test
├── .teamcity/
│   └── settings.kts                   # TeamCity configuration
├── Dockerfile                         # Container definition
├── docker-compose.yml                # Local deployment
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## 🎯 Why This Demo Works

### ✅ **Simple & Stable**
- Minimal dependencies
- No complex frameworks
- Proven technology stack

### ✅ **TeamCity Focused**
- Demonstrates core CI/CD features
- Clear build pipeline
- Easy to understand workflow

### ✅ **Demo Friendly**
- Fast build times (< 2 minutes)
- Reliable execution
- Clear success/failure indicators

### ✅ **Professional Looking**
- Modern UI with Bootstrap
- REST API documentation
- Docker containerization

## 🛠️ Customization

### Add New Features
1. Extend the `Product` entity
2. Add new API endpoints in `ProductController`
3. Update frontend in `static/` folder
4. Add corresponding tests

### Modify TeamCity Pipeline
1. Edit `.teamcity/settings.kts`
2. Add new build steps or configurations
3. Customize artifact rules
4. Adjust quality thresholds

## 📞 Support

This project is designed for TeamCity demonstration purposes. For production use, consider:
- Adding proper security
- Using external database
- Implementing proper error handling
- Adding comprehensive logging

---

**🎉 Perfect for TeamCity Demos!**  
Simple, stable, and showcases all the important CI/CD features.