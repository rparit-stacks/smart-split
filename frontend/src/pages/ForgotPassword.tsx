import { useState } from "react";
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { Mail } from "lucide-react";
import Logo from "@/components/Logo";
import AuthCard from "@/components/AuthCard";
import InputField from "@/components/InputField";
import AuthButton from "@/components/AuthButton";
import { useToast } from "@/hooks/use-toast";
import { authApi } from "@/lib/api";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";

const ForgotPassword = () => {
  const [step, setStep] = useState<"email" | "otp" | "reset">("email");
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState<{ email?: string; otp?: string; password?: string; confirmPassword?: string }>({});
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSendOtp = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!email) {
      setErrors({ email: "Email is required" });
      return;
    }
    
    if (!/\S+@\S+\.\S+/.test(email)) {
      setErrors({ email: "Please enter a valid email" });
      return;
    }
    
    setIsLoading(true);
    const response = await authApi.forgotPassword({ email });
    setIsLoading(false);
    
    if (response.error) {
      toast({
        title: "Failed",
        description: response.error,
        variant: "destructive",
      });
    } else {
      toast({
        title: "OTP sent!",
        description: "Please check your email.",
      });
      setStep("otp");
    }
  };

  const handleVerifyOtp = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (otp.length !== 6) {
      setErrors({ otp: "Please enter a 6-digit OTP" });
      return;
    }
    
    setStep("reset");
  };

  const handleResetPassword = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const newErrors: { password?: string; confirmPassword?: string } = {};
    
    if (!newPassword) {
      newErrors.password = "Password is required";
    } else if (newPassword.length < 8) {
      newErrors.password = "Password must be at least 8 characters";
    }
    
    if (!confirmPassword) {
      newErrors.confirmPassword = "Please confirm your password";
    } else if (newPassword !== confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }
    
    setErrors(newErrors);
    if (Object.keys(newErrors).length > 0) return;
    
    setIsLoading(true);
    const response = await authApi.resetPassword({ email, otp, newPassword });
    setIsLoading(false);
    
    if (response.error) {
      toast({
        title: "Reset failed",
        description: response.error,
        variant: "destructive",
      });
    } else {
      toast({
        title: "Password reset!",
        description: "Your password has been reset successfully.",
      });
      navigate("/login");
    }
  };

  const handleResendOtp = async () => {
    setIsLoading(true);
    const response = await authApi.resendForgotPasswordOtp({ email });
    setIsLoading(false);
    
    if (response.error) {
      toast({
        title: "Failed to resend",
        description: response.error,
        variant: "destructive",
      });
    } else {
      toast({
        title: "OTP resent!",
        description: "Please check your email.",
      });
    }
  };

  return (
    <div className="min-h-screen bg-background safe-area-inset flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <div className="text-center mb-8">
            <Logo size="lg" />
            <h1 className="mt-6 text-2xl font-display font-bold text-foreground">
              {step === "email" && "Forgot password?"}
              {step === "otp" && "Verify OTP"}
              {step === "reset" && "Reset password"}
            </h1>
            <p className="mt-2 text-muted-foreground">
              {step === "email" && "Enter your email to receive a reset code"}
              {step === "otp" && `Enter the code sent to ${email}`}
              {step === "reset" && "Enter your new password"}
            </p>
          </div>

          <AuthCard>
            {step === "email" && (
              <form onSubmit={handleSendOtp} className="space-y-4">
                <InputField
                  label="Email"
                  type="email"
                  placeholder="you@example.com"
                  icon={<Mail size={18} />}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  error={errors.email}
                />
                <AuthButton type="submit" isLoading={isLoading} className="w-full">
                  Send OTP
                </AuthButton>
                <p className="text-center text-sm text-muted-foreground">
                  <Link to="/login" className="text-primary hover:underline">
                    Back to login
                  </Link>
                </p>
              </form>
            )}

            {step === "otp" && (
              <form onSubmit={handleVerifyOtp} className="space-y-6">
                <div className="flex justify-center">
                  <InputOTP maxLength={6} value={otp} onChange={setOtp}>
                    <InputOTPGroup>
                      <InputOTPSlot index={0} />
                      <InputOTPSlot index={1} />
                      <InputOTPSlot index={2} />
                      <InputOTPSlot index={3} />
                      <InputOTPSlot index={4} />
                      <InputOTPSlot index={5} />
                    </InputOTPGroup>
                  </InputOTP>
                </div>
                {errors.otp && (
                  <p className="text-sm text-destructive text-center">{errors.otp}</p>
                )}
                <AuthButton type="submit" isLoading={isLoading} className="w-full">
                  Verify OTP
                </AuthButton>
                <div className="text-center">
                  <button
                    type="button"
                    onClick={handleResendOtp}
                    disabled={isLoading}
                    className="text-sm text-primary hover:underline disabled:opacity-50"
                  >
                    Resend OTP
                  </button>
                </div>
              </form>
            )}

            {step === "reset" && (
              <form onSubmit={handleResetPassword} className="space-y-4">
                <InputField
                  label="New Password"
                  type="password"
                  placeholder="••••••••"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  error={errors.password}
                />
                <InputField
                  label="Confirm Password"
                  type="password"
                  placeholder="••••••••"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  error={errors.confirmPassword}
                />
                <AuthButton type="submit" isLoading={isLoading} className="w-full">
                  Reset Password
                </AuthButton>
              </form>
            )}
          </AuthCard>
        </motion.div>
      </div>
    </div>
  );
};

export default ForgotPassword;

