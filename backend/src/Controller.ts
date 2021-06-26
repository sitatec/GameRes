import { TokenManager } from "./TokenManager";
import {Request, Response} from "express"

export class Controller {

  constructor(private readonly _tokenManager = new TokenManager()){}

  readonly getToken = async (request :Request, response: Response) => {
    console.log("\nGET TOKEN CALLD\n");
    try{
      if(! await this._tokenManager.isCurrentTokenValid()){
        await this._tokenManager.generateNewToken();
      }
       response.json({ token: this._tokenManager.token})
    } catch {
      response.status(500).end();
    }
  };

  // readonly refreshToken = async (_: Request, response: Response) => {

  // };

}