# E-Commerce Product Management System
### TeamCity Demo Project

[![Build Status](https://teamcity.example.com/app/rest/builds/buildType:id:MainBuild/statusIcon)](https://teamcity.example.com/project/EcommerceDemo)
[![Quality Gate Status](https://sonarqube.example.com/api/project_badges/measure?project=ecommerce-product-management&metric=alert_status)](https://sonarqube.example.com/dashboard?id=ecommerce-product-management)
[![Coverage](https://sonarqube.example.com/api/project_badges/measure?project=ecommerce-product-management&metric=coverage)](https://sonarqube.example.com/dashboard?id=ecommerce-product-management)

A comprehensive e-commerce product management system designed to showcase TeamCity CI/CD capabilities, best practices, and enterprise-level development workflows.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React SPA     │    │  Spring Boot    │    │   PostgreSQL    │
│   (Frontend)    │◄──►│   (Backend)     │◄──►│   (Database)    │
│                 │    │                 │    │                 │
│ • TypeScript    │    │ • REST API      │    │ • JPA/Hibernate │
│ • Bootstrap     │    │ • Spring        │    │ • Flyway        │
│ • React Query   │    │ • Validation    │    │ • Indexes       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Features

### Product Management
- ✅ **CRUD Operations** - Create, read, update, delete products
- ✅ **Category Management** - Organize products into categories
- ✅ **Search & Filtering** - Advanced product search capabilities
- ✅ **Stock Management** - Inventory tracking and low-stock alerts
- ✅ **Validation** - Comprehensive input validation and error handling

### Technical Features
- ✅ **RESTful API** - OpenAPI/Swagger documented endpoints
- ✅ **Responsive UI** - Mobile-friendly React interface
- ✅ **Data Persistence** - PostgreSQL with H2 for testing
- ✅ **Database Migration** - Flyway version control
- ✅ **Security** - Spring Security integration
- ✅ **Monitoring** - Actuator health checks and metrics

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 2.7.18
- **Language**: Java 11
- **Database**: PostgreSQL 15 / H2 (testing)
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway
- **Security**: Spring Security
- **Documentation**: SpringDoc OpenAPI 3
- **Build**: Maven 3.8+

### Frontend
- **Framework**: React 18
- **Language**: TypeScript
- **UI**: Bootstrap 5 + React Bootstrap
- **HTTP Client**: Axios
- **State Management**: React Query
- **Routing**: React Router
- **Forms**: React Hook Form
- **Build**: Create React App

### DevOps & Quality
- **CI/CD**: TeamCity
- **Code Quality**: SonarQube
- **Code Coverage**: JaCoCo
- **Static Analysis**: SpotBugs, PMD, Checkstyle
- **Security**: OWASP Dependency Check, Snyk
- **Testing**: JUnit 5, Mockito, TestContainers, Selenium
- **Performance**: JMeter
- **Containerization**: Docker + Docker Compose
- **Monitoring**: Prometheus + Grafana

## 📋 Prerequisites

- **Java 11** or later
- **Node.js 18** or later
- **Docker** and Docker Compose
- **Maven 3.8+** (or use included wrapper)
- **TeamCity Server** (for CI/CD setup)

## 🏃‍♂️ Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/teamcity-demo/ecommerce-product-management.git
cd ecommerce-product-management
```

### 2. Backend Setup
```bash
# Using Maven wrapper
./mvnw clean install

# Run with development profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The backend will start on `http://localhost:8080`

- **API Documentation**: http://localhost:8080/api/swagger-ui/index.html
- **H2 Console**: http://localhost:8080/api/h2-console (dev profile)
- **Actuator Health**: http://localhost:8080/api/actuator/health

### 3. Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend will start on `http://localhost:3000`

### 4. Docker Setup (Recommended)
```bash
# Development environment
docker-compose -f docker-compose.dev.yml up

# Production environment
docker-compose up
```

## 🧪 Testing

### Run All Tests
```bash
# Backend tests
./mvnw clean test

# Integration tests
./mvnw clean verify

# Frontend tests
cd frontend && npm test

# End-to-end tests
./mvnw clean verify -Pe2e
```

### Test Coverage
```bash
# Generate coverage report
./mvnw clean test jacoco:report

# View coverage: target/site/jacoco/index.html
```

### Quality Checks
```bash
# Code style check
./mvnw checkstyle:check

# Static analysis
./mvnw spotbugs:check pmd:check

# SonarQube analysis
./mvnw sonar:sonar -Dsonar.host.url=http://localhost:9000
```

## 🔧 TeamCity Configuration

### Build Chain Overview
The project includes a comprehensive TeamCity build chain with 10 build configurations:

1. **Main Build** - Compile, test, package
2. **Quality Gate** - SonarQube, Checkstyle, SpotBugs, PMD
3. **Integration Tests** - TestContainers-based tests
4. **Frontend Build** - React build and tests
5. **E2E Tests** - Selenium WebDriver tests
6. **Docker Build** - Multi-stage container builds
7. **Security Scan** - OWASP + Snyk vulnerability scanning
8. **Performance Tests** - JMeter load testing
9. **Deploy Staging** - Automated staging deployment
10. **Deploy Production** - Manual production deployment

### Build Features
- ✅ **VCS Triggers** - Automatic builds on code changes
- ✅ **Pull Request Builds** - GitHub PR integration
- ✅ **Parallel Execution** - Optimized build chains
- ✅ **Artifact Dependencies** - Efficient artifact sharing
- ✅ **Build Templates** - Reusable configurations
- ✅ **Environment Variables** - Parameterized builds
- ✅ **Failure Conditions** - Smart failure handling
- ✅ **Build Metrics** - Performance tracking

### Setup Instructions

1. **Import Configuration**
   ```bash
   # Copy TeamCity settings to your TeamCity server
   cp -r .teamcity/* /path/to/teamcity/config/projects/
   ```

2. **Configure Parameters**
   - Set up VCS root with your repository URL
   - Configure Maven and JDK locations
   - Set up SonarQube integration
   - Configure Docker registry
   - Set environment-specific parameters

3. **Agent Requirements**
   - Linux agents with Docker support
   - JDK 11 and Maven 3.8+
   - Node.js 18+ for frontend builds
   - Chrome/ChromeDriver for E2E tests
   - JMeter for performance tests

## 📊 Monitoring & Observability

### Application Metrics
- **Health Checks**: http://localhost:8080/api/actuator/health
- **Metrics**: http://localhost:8080/api/actuator/metrics
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3001 (admin/admin)

### Build Metrics
TeamCity provides comprehensive build analytics:
- Build duration trends
- Test failure analysis
- Code coverage evolution
- Artifact size tracking
- Agent utilization

## 🔐 Security

### Implementation
- ✅ **Input Validation** - Bean validation with custom constraints
- ✅ **SQL Injection Protection** - JPA/Hibernate parameterized queries
- ✅ **XSS Protection** - Content Security Policy headers
- ✅ **CSRF Protection** - Spring Security default settings
- ✅ **Security Headers** - Comprehensive HTTP security headers

### Security Scanning
- **OWASP Dependency Check** - Vulnerability scanning
- **Snyk** - Real-time security monitoring
- **Container Scanning** - Trivy image analysis
- **Code Analysis** - SpotBugs security rules

## 📈 Performance

### Backend Optimizations
- ✅ **Connection Pooling** - HikariCP configuration
- ✅ **JPA Optimization** - Lazy loading and query optimization
- ✅ **Caching** - Application-level caching strategies
- ✅ **Database Indexes** - Strategic index placement

### Frontend Optimizations
- ✅ **Code Splitting** - React lazy loading
- ✅ **Bundle Optimization** - Webpack optimizations
- ✅ **Caching Strategy** - Static asset caching
- ✅ **Image Optimization** - Responsive image loading

### Performance Testing
- **JMeter Scripts** - Load and stress testing
- **Performance Thresholds** - Automated performance gates
- **Monitoring** - Real-time performance metrics

## 🐳 Docker

### Multi-stage Builds
```dockerfile
# Development build with hot reload
docker build --target development .

# Production optimized build
docker build --target production .
```

### Container Orchestration
```bash
# Full stack with monitoring
docker-compose up

# Development with debugging
docker-compose -f docker-compose.dev.yml up

# Production deployment
docker-compose -f docker-compose.prod.yml up -d
```

## 📚 API Documentation

### Interactive Documentation
- **Swagger UI**: http://localhost:8080/api/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8080/api/v3/api-docs

### Key Endpoints
```
GET    /api/v1/products              # List products
POST   /api/v1/products              # Create product
GET    /api/v1/products/{id}         # Get product
PUT    /api/v1/products/{id}         # Update product
DELETE /api/v1/products/{id}         # Delete product
GET    /api/v1/products/search       # Search products
GET    /api/v1/categories            # List categories
```

## 🤝 Contributing

### Development Workflow
1. Create feature branch from `main`
2. Implement changes with tests
3. Run quality checks locally
4. Submit pull request
5. TeamCity validates PR
6. Code review and merge

### Code Standards
- **Java**: Google Java Style Guide
- **TypeScript**: Prettier + ESLint
- **Testing**: Minimum 80% coverage
- **Documentation**: JavaDoc + TSDoc

## 🎯 TeamCity Features Demonstrated

### Build Configuration
- [x] **Build Steps** - Maven, Node.js, Docker, Script
- [x] **Build Triggers** - VCS, Schedule, Manual
- [x] **Build Templates** - Reusable configurations
- [x] **Build Chains** - Sequential and parallel execution
- [x] **Artifact Dependencies** - Cross-build artifact sharing

### Testing Integration
- [x] **Unit Tests** - JUnit + Jest execution
- [x] **Integration Tests** - TestContainers
- [x] **E2E Tests** - Selenium automation
- [x] **Performance Tests** - JMeter integration
- [x] **Test Reports** - HTML reports and trends

### Quality Assurance
- [x] **Code Coverage** - JaCoCo integration
- [x] **Static Analysis** - Multiple tools integration
- [x] **Security Scanning** - OWASP + Snyk
- [x] **SonarQube** - Quality gate enforcement
- [x] **Docker Security** - Container vulnerability scanning

### Deployment Pipeline
- [x] **Multi-Environment** - Dev, staging, production
- [x] **Docker Builds** - Multi-stage builds
- [x] **Health Checks** - Application monitoring
- [x] **Rollback Strategy** - Automated rollback capabilities
- [x] **Blue-Green Deployment** - Zero-downtime deployments

### Advanced Features
- [x] **VCS Integration** - Git branch strategies
- [x] **Pull Request Builds** - GitHub integration
- [x] **Build Notifications** - Multiple channels
- [x] **Build Analytics** - Comprehensive metrics
- [x] **Agent Management** - Requirements and pools

## 📞 Support

For TeamCity-related questions:
- 📧 Email: support@example.com
- 📚 Documentation: [Internal Wiki](https://wiki.example.com)
- 🎫 Issues: [GitHub Issues](https://github.com/teamcity-demo/ecommerce-product-management/issues)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <strong>🚀 Built with ❤️ for TeamCity Excellence</strong><br>
  Demonstrating enterprise-level CI/CD best practices
</div>