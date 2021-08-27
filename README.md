# README

This project is intended as a backend for an online artwork buying and selling site.

Endpoints:

Get all users

Get single user

Get all paintings

Get single painting

Get all userActions

Get single UserAction

Get all paintingActions

Get single paintingAction

Post new user

Post new painting

Post new userAction

Post new paintingAction

Upon the creation of a new valid userAction, a deposit or withdrawl occurs.

Upon the creation of a new valid paintingAction, a painting is either taken on or off the market, or it is sold and its ownership transferred.

# Tech Used

Cassandra, Reactor Netty, Maven, log4j, junit, Spring

# Syntax examples:

curl localhost:8080/paintingActivity

curl localhost:8080/paintingActivity/1

curl localhost:8080/users

curl localhost:8080/users/1

curl localhost:8080/paintings

curl localhost:8080/paintings/1

curl -X POST localhost:8080/user_id=201,username=artlover,password=racecar,balance=0

curl -X POST localhost:8080/painting_id=301,owner=103,title=happysun,url=painting.jpg,desc=crayonDrawing,author=mrboots,forSale=false,price=0

curl -X POST localhost:8080/action_id=1,user_id=101,action=deposit,amount=100

curl -X POST localhost:8080/action_id=311,user_id=103,painting_id=3,action=putOnSale,amount=100

curl -X POST localhost:8080/action_id=312,user_id=102,painting_id=3,action=takeOffSale,amount=100

curl -X POST localhost:8080/action_id=312,user_id=102,painting_id=3,action=buy,amount=100

# Incomplete features

Currently the balance of users is not updated on userActions and paintingActions

# To run

mvn exec - no arguments needed

Cassandra must already be running on port 9042
