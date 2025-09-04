import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import EmployeePage from "./pages/EmployeePage";
import AttendancePage from "./pages/AttendancePage";
import PayrollPage from "./pages/PayrollPage";
import Dashboard from "./pages/Dashboard";

export default function App() {
  return (
    <div>
      <nav className="bg-white shadow">
        <div className="container flex items-center justify-between">
          <div className="py-3">
            <Link to="/" className="font-bold text-lg">EMS</Link>
          </div>
          <div className="space-x-4">
            <Link to="/" className="hover:underline">Dashboard</Link>
            <Link to="/employees" className="hover:underline">Employees</Link>
            <Link to="/attendance" className="hover:underline">Attendance</Link>
            <Link to="/payroll" className="hover:underline">Payroll</Link>
          </div>
        </div>
      </nav>
      <main className="container mt-6">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/employees" element={<EmployeePage />} />
          <Route path="/attendance" element={<AttendancePage />} />
          <Route path="/payroll" element={<PayrollPage />} />
        </Routes>
      </main>
    </div>
  );
}
