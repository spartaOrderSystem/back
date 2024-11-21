# Sparta Order System

### 주문 관리 플랫폼
- 배달의 민족의 주문 방식을 기반으로 AI를 접목
- 기존에 가게 주인은 상품 설명 작성에 시간이 소요되었고, 고객은 상품 정보가 부족하여 구매 결정에 어려움을 겪었음
  AI를 사용하여 상품 설명을 생성함으로써 기존 방식의 비효율성을 해결


  
### ERD 
![1_주문 시스템 프로젝트 erd](https://github.com/user-attachments/assets/d3ccf4d4-f674-42f6-8e55-c06ebe240123)




### API 설계
https://teamsparta.notion.site/API-384fcbad127648b5999562b3ea679bed
- 주요기능
  - 카테고리 : 등록, 조회, 수정
  - 가게 : 등록, 조회, 수정, 삭제, 조건부 검색
  - 메뉴 : 등록, 조회, 수정, 삭제, 조건부 검색, AI 기반 설명글 생성
  - 주문 : 등록, 조회, 수정
  - 그 외 사용자, 리뷰, 결제 도메인의 주요기능들 구현


### 아키텍처 설계
![image](https://github.com/user-attachments/assets/f1974a34-f4e8-45c9-a547-116b673d826d)





### 서비스 구성 및 실행방법
- JAVA 17
- PostgreSQL 15
- Docker 와 Docker Compose

> 백엔드 실행 \
> 인텔리제이 터미널 \
> git clone https://github.com/spartaOrderSystem/back.git  

![image](https://github.com/user-attachments/assets/0cfe57e3-bc5e-44a7-8db1-197aa9482109)


포스트맨에서 테스트 시 위 순서대로 실행 후 오더 및 리뷰, 결제 기능을 사용해야합니다.





### 기술 스택
#### 백엔드
- JAVA17
- Spring Boot 3.3.x
  - Spring Security
  - Spring Data JPA
  - QueryDsl
- PostgreSQL

#### 인프라
- Docker, Docker Compose
- AWS EC2

#### 협업 도구
- GIT, GitHub
- SLACK
- Postman
- Notion

#### API
- GEMINI API
