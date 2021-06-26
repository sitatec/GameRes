import { TokenManager } from "./TokenManager";
import {Request, Response} from "express"

export class Controller {
  private readonly _tokenManager = new TokenManager();

  getToken = async (_ :Request, response: Response) => {
    response.json({
      token: await this._tokenManager.getToken()
    })
  };
}