import { env } from "process";
import bent from "bent";

export class TokenManager {
  private _token = "";
  private _httpClient = bent("json")

  
  public async getToken () {
    if(!this._token){
      await this.generateToken();
    }
    return this._token;
  }

  private generateToken() {
    return this._httpClient(this.getApiTokenUrl()).then((response) =>{
      this._token = response.access_token;
      // Reset the current token 10 second before it expires.
      setTimeout(() => this._token = "", response.expires_in - 10);
    })
  }

  private getApiTokenUrl(){
    return `https://id.twitch.tv/oauth2/token?client_id=${env.CLIENT_ID}&client_secret=${env.CLIENT_SECRET}&grant_type=client_credentials`;
  }
  
}