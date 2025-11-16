import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import axios from "axios";

function Signup() {
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [greet, setGreet] = useState(false);
  const navigate = useNavigate();

  // Default role always ROLE_CUSTOMER
  const selectedRole = "ROLE_CUSTOMER";

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (password.length <= 5) {
      setError("Password must be longer than 5 characters.");
      return;
    }

    if (!/^\d{10}$/.test(phone)) {
      setError("Phone number must be exactly 10 digits.");
      return;
    }

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    if (!name || !phone || !email || !address) {
      setError("All fields are required.");
      return;
    }

    const newUser = {
      username: name,
      phone,
      email,
      address,
      password,
      roleType: selectedRole,
    };

    try {
      const response = await axios.post(
        "http://localhost:8081/api/v1/user-service/register",
        newUser
      );
      localStorage.setItem("user", JSON.stringify(response.data));
      setError("");
      setGreet(true);
      setTimeout(() => {
        setGreet(false);
        navigate("/login");
      }, 1500);
    } catch (err) {
      console.log("Signup error:", err.response);
      setError(
        err.response?.data?.message || "An error occurred. Please try again."
      );
    }
  };

  return (
    <div className="relative h-screen w-full overflow-hidden">
      {greet && (
        <div className="fixed top-8 left-1/2 transform -translate-x-1/2 bg-green-600 text-white px-6 py-3 rounded-lg shadow-lg z-50 text-lg font-semibold">
          Signup Successful!
        </div>
      )}

      <video
        autoPlay
        loop
        muted
        playsInline
        className="absolute inset-0 w-full h-full object-cover z-0"
      >
        <source src="/videos/vid.mp4" type="video/mp4" />
        Your browser does not support the video tag.
      </video>

      <div className="absolute inset-0 bg-black opacity-50 z-0"></div>
      <Navbar />

      <div className="relative flex items-center justify-center min-h-screen z-10 px-4">
        <div className="bg-black/40 backdrop-blur-md shadow-2xl rounded-2xl p-6 w-full max-w-[420px] md:max-w-[468px] min-h-[500px] text-white flex flex-col">
          <h2 className="text-2xl font-bold text-center mb-4">Sign Up</h2>

          {error && (
            <p className="text-red-400 text-center mb-4 font-medium">{error}</p>
          )}

          <form
            onSubmit={handleSubmit}
            className="flex flex-col flex-grow h-full justify-between"
          >
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 flex-grow">
              {/* Full Name */}
              <div>
                <label className="block mb-1 font-medium">Full Name</label>
                <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Enter your name"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>

              {/* Email */}
              <div>
                <label className="block mb-1 font-medium">Email</label>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Enter your email"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>

              {/* Address */}
              <div>
                <label className="block mb-1 font-medium">Address</label>
                <input
                  type="text"
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                  placeholder="Enter your address"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>

              {/* Phone Number */}
              <div>
                <label className="block mb-1 font-medium">Phone Number</label>
                <input
                  type="tel"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="Enter 10-digit phone number"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>

              {/* Password */}
              <div>
                <label className="block mb-1 font-medium">Password</label>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter your password"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>

              {/* Confirm Password */}
              <div>
                <label className="block mb-1 font-medium">Confirm Password</label>
                <input
                  type="password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Confirm your password"
                  className="w-full border border-gray-600 bg-transparent rounded-lg px-3 py-2 focus:ring-2 focus:ring-red-500 focus:outline-none placeholder-gray-400"
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              className="w-full bg-red-600 text-white font-semibold py-2 rounded-lg hover:bg-red-700 transition mt-6"
            >
              Sign Up
            </button>
          </form>

          <p className="text-center text-gray-300 mt-4">
            Already have an account?{" "}
            <span
              onClick={() => navigate("/login")}
              className="text-red-400 font-medium cursor-pointer hover:underline"
            >
              Login
            </span>
          </p>
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default Signup;
