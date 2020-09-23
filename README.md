# Introduction 
Test example of real-time bidding agent

# Instruction

Run `sbt test` to run project unit tests

Run `sbt reStart` to start an Akka HTTP server, and run the application.

At the shell enter following line to send the request:

`curl -H "Content-type: application/json" -X POST -d '{"id": "SGu1Jpq1IO","site": {"id": "0006a522ce0f4bbbbaa6b3c38cafaa0f","domain": "fake.tld"},
"device": {"id": "440579f4b408831516ebd02f6e1c31b4","geo": {"country": "LT"}},"imp": [{"id": "1","wmin": 50,"wmax": 300,"hmin": 100,"hmax": 300,
"h": 250,"w": 300,"bidFloor": 3.12123}],"user": {"geo": {"country": "LT"},"id": "USARIO1"}}' http://localhost:8080/bids` 

Run `sbt reStop` to stop an Akka HTTP server.
