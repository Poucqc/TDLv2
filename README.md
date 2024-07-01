1. refesh token 도입
  - access 토큰과 같이 로그인할떄 refresh 토큰을 발급하도록 하고 관리는 각각의 토큰의 subject를 "access" 와 "refresh" 로 설정해서 관리했습니다
2. Auth를 담당하는 AOP를 만들었습니다
  - 어노테이션의 형태로 만든 CustomAuth 가 spring security 대신 auth를 담당합니다
  - 해당 어노테이션에 (roles = "role") 을 붙여 role을 관리합니다
  - 해당 로직을 코틀린의 확장 함수를 이용한 체인 패턴으로 구성해봤습니다
3. OAuth2.0 다수 구현
  - google, naver, kakao 의 OAuth2.0 을 이용한 소셜 로그인을 구현했습니다
  - 각각의 client 와 response dto 는 전략 패턴을 이용한 추상화를 해봤습니다
  - auth2.0 요청은 코틀린의 확장 함수를 이용한 체인 패턴으로 구성해봤습니다
4. hashtag 구현
  - post 와 hastag를 중간 테이블을 사이에둔 다대다 관계로 설정했습니다
5. 엔티티의 역할
  - setter, getter 사용을 지양하기 위해 엔티티 내부의 필드를 private 로 설정했습니다
  - 이제 엔티티의 속성을 변경하는 역할은 엔티티만 수행 합니다 서비스에서 해당 로직을 수행하고 싶을때는 엔티티에서 설정한 내부 함수를 통해 수행 합니다
  - id 같은 경우는 예외로 private 하지 않게 했습니다 이유는 id는 해당 클래스의 생성자가 아닌 변수로서 설정하였고 읽기 전용 변수이기 때문에 밖으로 불러와도 값을 변경할 수 없을것으로 예상되기 떄문입니다
6. spring scheduler 도입
  - 정해진 시간, 혹은 정해진 주기마다 특정 로직을 실행 되도록 하는 스케쥴러를 도입 했습니다
  - 실행할 각 로직은 해당하는 서비스에 작성하고 그 메서드를 특정 시간마다 작동하게하는 설정은 scheduler 객체에서 담당합니다
7. soft delete 지원
 - 두가지 방식으로 구현해봤습니다
 - 1. 엔티티에 삭제 여부를 표현하는 필드를 구현하고 해당 필드가 삭제되었음 일때는 일반 유저가 불러올 수 없게 했습니다
   2. 삭제된 엔티티를 다른 곳에 저장하고 삭제 정보를 저장하도록 했습니다 (comment 삭제요청 -> comment는 삭제됨 -> 복제된 comment와 정보를 deleted comment 로 저장함 : 각기 다른 엔티티 형태로 저장됨)
8. 복합키 사용 엔티티
 - 복합키를 사용하는 엔티티를 만들어 봤습니다(like-likeId)
 - embededable 과 embededId 를 이용했습니다
9. redis 를 통한 email 검증
10. NotFoundExeption 을 AOP로 구현
  - 이부분은 아직 공부가 더 필요합니다

시간이 모자라서 구현 못한 것들
1. test code
2. admin logic
3. aws s3 ( 버킷까지는 진행됨 )
4. 배포
