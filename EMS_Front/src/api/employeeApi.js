import client from "./axiosClient";

export const getEmployees = () => client.get("/employees?page=0&size=5&sort=name,asc");
export const createEmployee = (data) => client.post("/employees", data);
export const updateEmployee = (id, data) => client.put(`/employees/${id}`, data);
export const deleteEmployee = (id) => client.delete(`/employees/${id}`);
