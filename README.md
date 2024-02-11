To start the project
```
docker compose up
```

Endpoint to start a game:  
localhost:8080/api/new  
JSON format:  
{
  "width": 5,
  "height": 5,
  "mines_count": 5
}    

Endpoint to make a move:  
localhost:8080/api/turn  
JSON format:  
{
  "game_id": "9670ae20-0d95-4b37-ad8a-05610524bce0",
  "row": 4,
  "col": 4
}  
game_id is returned once a game is started  


