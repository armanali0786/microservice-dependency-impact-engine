export type Role = "ADMIN" | "ARCHITECT" | "DEVELOPER" | "SRE" | "VIEWER";

export interface CurrentUser {
  id: string;
  email: string;
  fullName: string;
  roles: Role[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: CurrentUser;
}
