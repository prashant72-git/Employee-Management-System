import React, { useState } from "react";
import { checkIn, checkOut } from "../api/attendanceApi";

export default function AttendancePage() {
  const [employeeId, setEmployeeId] = useState("");

  const doCheckIn = async () => {
    try {
      await checkIn(Number(employeeId));
      alert("Checked in");
    } catch (e) { console.error(e); alert("Check-in failed: " + e?.response?.data); }
  };

  const doCheckOut = async () => {
    try {
      await checkOut(Number(employeeId));
      alert("Checked out");
    } catch (e) { console.error(e); alert("Check-out failed: " + e?.response?.data); }
  };

  return (
    <div className="card">
      <h3 className="font-semibold mb-2">Attendance</h3>
      <div className="grid grid-cols-3 gap-2 items-end">
        <input placeholder="Employee ID" className="border p-2" value={employeeId} onChange={e=>setEmployeeId(e.target.value)} />
        <button className="btn btn-primary" onClick={doCheckIn}>Check In</button>
        <button className="btn" onClick={doCheckOut}>Check Out</button>
      </div>
      <p className="mt-3 text-sm text-gray-600">Note: For demo, enter numeric employee id (e.g., 1 or 2) and click actions.</p>
    </div>
  );
}
