# 오목

자바 스윙을 이용한 네트워크 오목 프로그램.

1:1 대전이며 게임 진행 중 입장하는 사람들은 관전자가 되어 게임의 진행 상황을 관전할 수 있다.



## 클라이언트

![메인 화면](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.png)

![게임 화면](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/%EA%B2%8C%EC%9E%84%ED%99%94%EB%A9%B4.png)

> 메인 화면에서 회원가입을 한 뒤 로그인을 하면 게임 화면으로 진입할 수 있다.
>
> 아직 암호에 대한 암호화는 적용되어있지 않다.



![게임 참여](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/%EA%B2%8C%EC%9E%84%EC%B0%B8%EC%97%AC.png)

> 참여 버튼을 눌러 게임에 참여할 수 있으며 한 서버 당 2명이서만 참여가 가능하고 나머지는 관전을 할 수 있다.



![게임 시작](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/%EA%B2%8C%EC%9E%84%EC%8B%9C%EC%9E%91.png)

> 마우스 커서를 움직이면 착수 지점에 돌 이미지를 볼 수 있다.
>
> 왼쪽 참여 버튼이 흑이며 흑이 선이다.
>
> 렌주룰을 적용하여 흑은 특정 위치에 돌을 둘 수 없다.




![게임 시작](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/%EA%B2%8C%EC%9E%84%EC%8B%9C%EC%9E%91.png)

> 게임 참여자와 관전자 모두 채팅을 할 수 있다.



## 서버

![게임 시작](https://github.com/CPstudy/JavaOmok/blob/master/screenshot/서버.png)

> 서버에서는 클라이언트에 돌을 착수할 수 있는 기능이 있으며 서버에서 채팅창에 메시지를 보낼 수 있다.
>
> $stone {숫자}
>
> 숫자 부분에는 0(지우기), 1(흑), 2(백)을 넣으면 되고 돌 종류를 입력 후 오목판 원하는 위치에 클릭하면 해당 위치에 알맞는 이벤트가 작동된다.
