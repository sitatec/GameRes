import { TokenManager } from "./TokenManager";
import {Request, Response} from "express"

export class Controller {

  constructor(private readonly _tokenManager = new TokenManager()){}

  readonly getToken = async (_ :Request, response: Response) => {
    if(! await this._tokenManager.isCurrentTokenValid()){
      await this._tokenManager.generateNewToken();
    }
    response.json({
      token: this._tokenManager.token
    })
  };

  // readonly refreshToken = async (_: Request, response: Response) => {

  // };

}