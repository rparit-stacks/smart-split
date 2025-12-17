import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { useNavigate, Link } from "react-router-dom";
import { Mail } from "lucide-react";
import Logo from "@/components/Logo";
import AuthCard from "@/components/AuthCard";
import InputField from "@/components/InputField";
import AuthButton from "@/components/AuthButton";
import { useToast } from "@/hooks/use-toast";
import { authApi } from "@/lib/api";
import { useAuth } from "@/contexts/AuthContext";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";

const VerifyLoginOtp = () => {
  const [otp, setOtp] = useState("");
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();
  const { login } = useAuth();

  useEffect(() => {
    const pendingLogin = localStorage.getItem('pendingLogin');
    if (pendingLogin) {
      const data = JSON.parse(pendingLogin);
      setEmail(data.email);
    } else {
      navigate("/login");
    }
  }, [navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (otp.length !== 6) {
      toast({
        title: "Invalid OTP",
        description: "Please enter a 6-digit OTP",
        variant: "destructive",
      });
      return;
    }
    
    setIsLoading(true);
    
    const response = await authApi.verifyLoginOtp({ email, otp });
    
    setIsLoading(false);
    
    if (response.error) {
      toast({
        title: "Verification failed",
        description: response.error,
        variant: "destructive",
      });
    } else {
      // Extract name from response message
      const message = response.data || "";
      const nameMatch = message.match(/Welcome (.+)/);
      const name = nameMatch ? nameMatch[1] : "User";
      
      login(email, name);
      localStorage.removeItem('pendingLogin');
      
      toast({
        title: "Login successful!",
        description: "Welcome back!",
      });
      
      navigate("/dashboard");
    }
  };

  const handleResend = async () => {
    setIsLoading(true);
    const response = await authApi.resendLoginOtp({ email });
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
              Verify your email
            </h1>
            <p className="mt-2 text-muted-foreground">
              Enter the 6-digit code sent to {email}
            </p>
          </div>

          <AuthCard>
            <form onSubmit={handleSubmit} className="space-y-6">
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

              <AuthButton type="submit" isLoading={isLoading} className="w-full">
                Verify OTP
              </AuthButton>

              <div className="text-center space-y-2">
                <button
                  type="button"
                  onClick={handleResend}
                  disabled={isLoading}
                  className="text-sm text-primary hover:underline disabled:opacity-50"
                >
                  Resend OTP
                </button>
                <p className="text-sm text-muted-foreground">
                  <Link to="/login" className="text-primary hover:underline">
                    Back to login
                  </Link>
                </p>
              </div>
            </form>
          </AuthCard>
        </motion.div>
      </div>
    </div>
  );
};

export default VerifyLoginOtp;

