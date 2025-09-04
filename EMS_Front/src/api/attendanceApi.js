import client from "./axiosClient";

export const checkIn = (employeeId) => {
  const body = { employeeId, timestampUtc: new Date().toISOString() };
  return client.post("/attendance/check-in", body);
};
export const checkOut = (employeeId) => {
  const body = { employeeId, timestampUtc: new Date().toISOString() };
  return client.post("/attendance/check-out", body);
};
