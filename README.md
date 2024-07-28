실행 방법

# 공통
### JWT 계정용 환경변수
- IN28MINUTES_USERNAME
- IN28MINUTES_PASSWORD
DB 계정용 환경변수
- IN28MINUTES_DB_USER
- IN28MINUTES_DB_PASSWORD

### default 프로파일
로컬 IDE 에서 개발한다.
MySQL 서버가 필요하다.

### local-h2 프로파일
로컬 IDE 에서 h2 데이터베이스를 사용하여 개발한다.
프로파일 `local-h2`를 설정한다.

### test 프로파일
통합 테스트를 실행한다.

### MySQL 서버 실행을 위한 docker-compose
사전 요구사항
- Docker 설치
```bash
docker-compose -f docker-compose.yml up -d
```