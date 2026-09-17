import axios from "axios";

import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
} from "../types/auth";

const API_URL = "http://localhost:8080/api/auth";

export const login = async (
  credentials: LoginRequest
): Promise<LoginResponse> => {

  const response = await axios.post<LoginResponse>(
    `${API_URL}/login`,
    credentials
  );

  return response.data;
};

export const register = async (
  userData: RegisterRequest
): Promise<RegisterResponse> => {

  const response = await axios.post<RegisterResponse>(
    `${API_URL}/register`,
    userData
  );

  return response.data;
};