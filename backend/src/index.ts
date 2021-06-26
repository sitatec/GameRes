import app, { request, response } from "express"
import { env } from "process"
import { TokenManager } from "./TokenManager";

const tokenManager = new TokenManager();

app().get("/apitoken/:secret", async (request, response) => {

  if(request.params.secret != env.CLIENT_ID as string + env.CLIENT_SECRET){
    response.send(400) // BAD REQUEST
  }

  response.json({
    token: tokenManager.getToken()
  })

}).listen(env.PORT || 8080, () => console.log("SERVER STARTED!!"));
