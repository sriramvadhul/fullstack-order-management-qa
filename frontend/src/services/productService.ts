import api from "./api";

import type {
  Product,
} from "../types/product";

export interface ProductRequest {
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  category: string;
  active: boolean;
}

export const getAllProducts =
  async (): Promise<Product[]> => {

    const response =
      await api.get<Product[]>(
        "/products"
      );

    return response.data;
  };

export const createProduct =
  async (
    product: ProductRequest
  ): Promise<Product> => {

    const response =
      await api.post<Product>(
        "/products",
        product
      );

    return response.data;
  };

export const updateProduct =
  async (
    productId: number,
    product: ProductRequest
  ): Promise<Product> => {

    const response =
      await api.put<Product>(
        `/products/${productId}`,
        product
      );

    return response.data;
  };

export const deleteProduct =
  async (
    productId: number
  ): Promise<void> => {

    await api.delete(
      `/products/${productId}`
    );
  };