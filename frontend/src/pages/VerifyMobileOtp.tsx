import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { useNavigate, Link } from "react-router-dom";
import Logo from "@/components/Logo";
import AuthCard from "@/components/AuthCard";
import AuthButton from "@/components/AuthButton";
import { useToast } from "@/hooks/use-toast";
import { authApi } from "@/lib/api";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";

const VerifyMobileOtp = () => {
  const [otp, setOtp] = useState("");
  const [phNumber, setPhNumber] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  useEffect(() => {
    const pendingVerification = localStorage.getItem('pendingVerification');
    if (pendingVerification) {
      const data = JSON.parse(pendingVerification);
      setPhNumber(data.phNumber);
    } else {
      navigate("/signup");
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
    
    const response = await authApi.verifyMobileOtp({ phNumber, otp });
    
    setIsLoading(false);
    
    if (response.error) {
      toast({
        title: "Verification failed",
        description: response.error,
        variant: "destructive",
      });
    } else {
      toast({
        title: "Verification complete!",
        description: "Your account has been verified successfully.",
      });
      localStorage.removeItem('pendingVerification');
      navigate("/login");
    }
  };

  const handleResend = async () => {
    setIsLoading(true);
    const response = await authApi.resendMobileOtp({ phNumber });
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
        description: "Please check your phone.",
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
              Verify your mobile
            </h1>
            <p className="mt-2 text-muted-foreground">
              Enter the 6-digit code sent to {phNumber}
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
                Verify Mobile OTP
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
                    Go to login
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

export default VerifyMobileOtp;

