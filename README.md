

<h1>🚀 11조 - Spring Backend 아웃소싱 관리 시스템</h1>

<p><strong>팀원</strong> : 11조 (Spring Boot 백엔드 협업)</p>
<p><strong>프로젝트 기간</strong> : 2024.04.22~29

<hr>

<h2>📚 프로젝트 소개</h2>
<p>저희 11조는 <strong>Spring Boot</strong>를 기반으로 외주 작업(아웃소싱) 관리 시스템의 백엔드를 구축하였습니다.<br>
본 프로젝트는 프론트엔드 없이 <strong>API 서버</strong>만 구축하며, 실제 외주 환경에서 필요한 핵심 기능을 구현하는 것을 목표로 진행했습니다.</p>
<p><strong>회원가입, 로그인, 메뉴 관리, 가게 관리, 주문, 리뷰</strong> 등 다양한 기능을 설계하고 직접 구현하여<br>
백엔드 협업 및 RESTful API 설계 경험을 쌓는 데 집중했습니다.</p>

<p>📌 [적용화면 캡처 삽입 예정]</p>

<hr>

<h2>🎯 개발 목표</h2>
<ul>
  <li>팀원 간 역할 분담과 협업을 통해 실제 서비스 형태에 가까운 백엔드 구축</li>
  <li>Spring Boot 기반 MVC 설계와 계층 분리(Controller-Service-Repository) 실습</li>
  <li>JPA를 통한 데이터베이스 연동 및 관리</li>
  <li>RESTful API 명세를 기반으로 한 API 설계 및 구현</li>
</ul>

<hr>

<h2>🛠️ 기술 스택</h2>
<table border="1">
  <thead>
    <tr><th>구분</th><th>사용 기술</th></tr>
  </thead>
  <tbody>
    <tr><td>Language</td><td>Java 17</td></tr>
    <tr><td>Framework</td><td>Spring Boot 3.x</td></tr>
    <tr><td>ORM</td><td>Spring Data JPA</td></tr>
    <tr><td>Database</td><td>MySQL 8.x</td></tr>
    <tr><td>Build Tool</td><td>Maven</td></tr>
    <tr><td>IDE</td><td>IntelliJ IDEA</td></tr>
    <tr><td>Version Control</td><td>Git / GitHub</td></tr>
  </tbody>
</table>

<hr>

<h2>📂 프로젝트 구조</h2>
<pre>
src/
├── main/
│   ├── java/
│   │   └── com/example/outsourcing_11/src/main
│   │       ├── controller/     # 요청을 처리하는 컨트롤러
│   │       ├── service/         # 비즈니스 로직을 담당하는 서비스
│   │       ├── repository/      # DB 접근을 담당하는 레포지토리
│   │                
│   └── resources/
│       ├── static/              # 정적 파일
│       ├── templates/           # (프론트 미사용)
│       └── application.properties # 환경설정 파일
</pre>

<hr>

<h2>🛠️ 주요 기능</h2>

<h3>1. 사용자(User)</h3>
<ul>
  <li>회원가입 (Signup)</li>
  <li>로그인 (Login)</li>
  <li>프로필 변경(주소,비밀번호 )</li>
  <li>유저 삭제/ 유저 수 조회</li>
</ul>

<h3>2. 메뉴(Menu)</h3>
<ul>
  <li>메뉴 등록</li>
  <li>메뉴 수정</li>
  <li>메뉴 삭제</li>
  <li>메뉴 목록 조회</li>
</ul>

<h3>3. 가게(Store)</h3>
<ul>
  <li>가게 등록/수정/삭제</li>
  <li>가게 목록 조회(단건 조회시 메뉴리스트 포함)</li>
  <li>가게 매출/ 고객 기간별 조회</li>
  <li>공지사항 등록/수정/삭제(사장님 권한)</li>
  <li>즐겨찾기 등록/수정/삭제</li>
</ul>

<h3>4. 주문(Order)</h3>
<ul>
  <li>주문 생성</li>
  <li>주문 목록 조회</li>
  <li>주문 상세 조회</li>
  <li>장바구니 기능(결제)</li>
</ul>

<h3>5. 리뷰(Review)</h3>
<ul>
  <li>리뷰 작성</li>
  <li>리뷰 조회</li>
  <li>리뷰 수정</li>
  <li>리뷰 삭제</li>
  <li>사장님 댓글 기능</li>
</ul>

<hr>

<h2>📑 API 명세 요약 (기능 일부)</h2>

<h3>🔐 User API</h3>
<table border="1">
  <thead>
    <tr><th>메서드</th><th>URL</th><th>설명</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td>/auth/signup</td><td>회원가입</td></tr>
    <tr><td>POST</td><td>/auth/login</td><td>로그인</td></tr>
    <tr><td>PATCH</td><td>/user/{id}</td><td>프로필 변경</td></tr>
  </tbody>
</table>

<h3>🍔 Menu API</h3>
<table border="1">
  <thead>
    <tr><th>메서드</th><th>URL</th><th>설명</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td>/admin/{storeId}/menu</td><td>메뉴 등록</td></tr>
    <tr><td>PATCH</td><td>/admin/{storeId}/menu/{menuId}</td><td>메뉴 수정</td></tr>
    <tr><td>DELETE</td><td>/admin/{storeId}/menu/{menuId}</td><td>메뉴 삭제</td></tr>
    <tr><td>GET</td><td>/menu?name ={name}</td><td>메뉴 조회</td></tr>
  </tbody>
</table>

<h3>🏪 Store API</h3>
<table border="1">
  <thead>
    <tr><th>메서드</th><th>URL</th><th>설명</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td>/stores</td><td>가게 등록</td></tr>
    <tr><td>PUT</td><td>/stores/{storeId}</td><td>가게 수정</td></tr>
    <tr><td>DELETE</td><td>/stores/{storeId}</td><td>가게 삭제</td></tr>
    <tr><td>GET</td><td>/stores</td><td>가게 조회</td></tr>
    <tr><td>POST</td><td>/stores/{storeId}/notice</td></td><td>공지 등록</td></tr>
  </tbody>
</table>

<h3>🛒 Order API</h3>
<table border="1">
  <thead>
    <tr><th>메서드</th><th>URL</th><th>설명</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td>/orders</td><td>주문 생성</td></tr>
    <tr><td>GET</td><td>/orders</td><td>주문 목록 조회</td></tr>
    <tr><td>GET</td><td>/orders/{orderId}</td><td>주문 상세 조회</td></tr>
    <tr><td>GET</td><td>/carts/{userId}</td><td>장바구니 목록 조회</td></tr>
    <tr><td>POST</td><td>/orders/price</td><td>주문 결제</td></tr>
  </tbody>
</table>

<h3>⭐ Review API</h3>
<table border="1">
  <thead>
    <tr><th>메서드</th><th>URL</th><th>설명</th></tr>
  </thead>
  <tbody>
    <tr><td>POST</td><td>/reviews</td><td>리뷰 작성</td></tr>
    <tr><td>GET</td><td>/reviews</td><td>리뷰 조회</td></tr>
    <tr><td>PATCH</td><td>/reviews/{reviewId}</td><td>리뷰 수정</td></tr>
    <tr><td>DELETE</td><td>/reviews/{reviewId}</td><td>리뷰 삭제</td></tr>
    <tr><td>POST</td><td>stores/{storeId}/comments{commentId}/ownerReplys</td><td>리뷰에 사장님 댓글 작성</td></tr>
    
  </tbody>
</table>

<hr>

<h2>🚀 개발 진행 방법</h2>
<ul>
  <li>Github Flow 방식 사용 (dev → main 브랜치 머지)</li>
  <li>API 설계 회의를 통한 명세 통일</li>
  <li>Postman을 이용한 수시 테스트</li>
</ul>

<hr>

<h2>🖼️ 적용 화면 (추후 캡처 추가 예정)</h2>
<ul>
  <li>회원가입/로그인 API 테스트 화면</li>
  <li>메뉴 등록 및 조회 API 테스트 화면</li>
  <li>가게 등록 및 조회 API 테스트 화면</li>
  <li>주문 생성 및 조회 API 테스트 화면</li>
  <li>리뷰 작성 및 조회 API 테스트 화면</li>
</ul>

<hr>

<h2>🙌 팀원 역할 분담</h2>
<table border="1">
  <thead>
    <tr><th>이름</th><th>담당 역할</th></tr>
  </thead>
  <tbody>
    <tr><td>김석진</td><td>회원(User) 기능 개발</td></tr>
    <tr><td>김지환</td><td>메뉴(Menu) 기능 개발</td></tr>
    <tr><td>정은세</td><td>가게(Store) 기능 개발</td></tr>
    <tr><td>김형진</td><td>리뷰(Review) 기능 개발</td></tr>
    <tr><td>남유리</td><td>주문(Order) 기능 개발</td></tr>
  </tbody>
</table>

<hr>

<h2>📝 느낀 점</h2>
<blockquote>
Spring Boot를 이용해 API 서버를 구축하며, 계층 분리와 협업의 중요성을 실감할 수 있었습니다.<br>
비록 배우는 단계지만, API 설계, DB 연결, 오류처리 등 실제 서비스를 만들기 위한 기본기를 탄탄히 다질 수 있었습니다.<br>
앞으로 더 성장하는 11조가 되겠습니다!
</blockquote>

<h1>🔥 11조 화이팅!</h1>

</body>
</html>
