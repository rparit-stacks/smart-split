import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { Link, useNavigate } from "react-router-dom";
import { Mail, Lock, User, Phone } from "lucide-react";
import Logo from "@/components/Logo";
import AuthCard from "@/components/AuthCard";
import InputField from "@/components/InputField";
import AuthButton from "@/components/AuthButton";
import SocialButton from "@/components/SocialButton";
import Divider from "@/components/Divider";
import { useToast } from "@/hooks/use-toast";
import { authApi } from "@/lib/api";
import { useAuth } from "@/contexts/AuthContext";

const Signup = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phNumber, setPhNumber] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<{ name?: string; email?: string; phNumber?: string; password?: string }>({});
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();
  const { isAuthenticated } = useAuth();

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/dashboard", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Don't render if already authenticated
  if (isAuthenticated) {
    return null;
  }

  const validateForm = () => {
    const newErrors: { name?: string; email?: string; phNumber?: string; password?: string } = {};
    
    if (!name) {
      newErrors.name = "Name is required";
    } else if (name.length < 2) {
      newErrors.name = "Name must be at least 2 characters";
    }
    
    if (!email) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = "Please enter a valid email";
    }
    
    if (!phNumber) {
      newErrors.phNumber = "Phone number is required";
    } else if (phNumber.length < 10) {
      newErrors.phNumber = "Please enter a valid phone number";
    }
    
    if (!password) {
      newErrors.password = "Password is required";
    } else if (password.length < 8) {
      newErrors.password = "Password must be at least 8 characters";
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    e.stopPropagation();
    
    console.log('🔵 ========== SIGNUP FORM SUBMITTED ==========');
    console.log('👤 Name:', name);
    console.log('📧 Email:', email);
    console.log('📱 Phone:', phNumber);
    console.log('🔒 Password length:', password.length);
    console.log('📋 Current errors:', errors);
    
    const isValid = validateForm();
    console.log('✅ Validation result:', isValid);
    console.log('📋 Errors after validation:', errors);
    
    if (!isValid) {
      console.log('❌ Form validation failed - stopping submission');
      return;
    }
    
    console.log('✅ Form validated, making API call...');
    setIsLoading(true);
    
    try {
      console.log('📤 Calling register API with data:', { name, email, phNumber, password: '***' });
      const response = await authApi.register({ name, email, phNumber, password });
      console.log('📥 Register API response:', response);
      
      setIsLoading(false);
      
      if (response.error) {
        console.error('❌ Registration error:', response.error);
        toast({
          title: "Registration failed",
          description: response.error,
          variant: "destructive",
        });
      } else {
        console.log('✅ Registration success, navigating to OTP page');
        toast({
          title: "Registration successful!",
          description: "Please verify your email with the OTP sent to your email.",
        });
        // Store email and phone for OTP verification
        localStorage.setItem('pendingVerification', JSON.stringify({ email, phNumber }));
        navigate("/verify-email-otp");
      }
    } catch (error) {
      console.error('💥 Registration exception:', error);
      setIsLoading(false);
      toast({
        title: "Error",
        description: "An unexpected error occurred. Please try again.",
        variant: "destructive",
      });
    }
  };


  return (
    <div className="min-h-screen bg-background safe-area-inset">
      {/* Mobile Layout */}
      <div className="md:hidden flex flex-col min-h-screen">
        <motion.div
          className="gradient-hero pt-12 pb-16 px-6"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.5 }}
        >
          <Logo size="lg" />
          <motion.h1
            className="mt-8 text-3xl font-display font-bold text-primary-foreground"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
          >
            Create account
          </motion.h1>
          <motion.p
            className="mt-2 text-primary-foreground/80"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
          >
            Join thousands splitting expenses smarter
          </motion.p>
        </motion.div>

        <div className="flex-1 -mt-8 px-4 pb-8">
          <AuthCard>
            <form onSubmit={handleSubmit} className="space-y-4">
              <InputField
                label="Full Name"
                type="text"
                placeholder="John Doe"
                icon={<User size={18} />}
                value={name}
                onChange={(e) => setName(e.target.value)}
                error={errors.name}
              />
              
              <InputField
                label="Email"
                type="email"
                placeholder="you@example.com"
                icon={<Mail size={18} />}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                error={errors.email}
              />
              
              <InputField
                label="Phone Number"
                type="tel"
                placeholder="+1234567890"
                icon={<Phone size={18} />}
                value={phNumber}
                onChange={(e) => setPhNumber(e.target.value)}
                error={errors.phNumber}
              />
              
              <InputField
                label="Password"
                type="password"
                placeholder="••••••••"
                icon={<Lock size={18} />}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                error={errors.password}
              />

              <AuthButton 
                type="submit" 
                isLoading={isLoading}
              >
                Create Account
              </AuthButton>
            </form>

            <Divider text="or continue with" />

            <div className="space-y-3">
              <SocialButton
                icon={
                  <svg className="w-5 h-5" viewBox="0 0 24 24">
                    <path
                      fill="currentColor"
                      d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                    />
                    <path
                      fill="currentColor"
                      d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                    />
                    <path
                      fill="currentColor"
                      d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                    />
                    <path
                      fill="currentColor"
                      d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                    />
                  </svg>
                }
              >
                Continue with Google
              </SocialButton>
            </div>

            <p className="mt-6 text-center text-sm text-muted-foreground">
              Already have an account?{" "}
              <Link to="/login" className="text-primary font-semibold hover:underline">
                Sign in
              </Link>
            </p>

            <p className="mt-4 text-center text-xs text-muted-foreground">
              By signing up, you agree to our{" "}
              <a href="#" className="text-primary hover:underline">
                Terms of Service
              </a>{" "}
              and{" "}
              <a href="#" className="text-primary hover:underline">
                Privacy Policy
              </a>
            </p>
          </AuthCard>
        </div>
      </div>

      {/* Desktop Layout */}
      <div className="hidden md:flex min-h-screen">
        {/* Left Panel - Branding */}
        <motion.div
          className="w-1/2 gradient-hero flex flex-col justify-center px-12 lg:px-20"
          initial={{ opacity: 0, x: -50 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6 }}
        >
          <Logo size="lg" />
          <h1 className="mt-12 text-4xl lg:text-5xl font-display font-bold text-primary-foreground leading-tight">
            Start splitting<br />
            <span className="text-primary-foreground/80">like a pro</span>
          </h1>
          <p className="mt-6 text-lg text-primary-foreground/80 max-w-md">
            Join thousands of users who trust SmartSplit to manage their 
            shared expenses. It's free to get started.
          </p>
          
          <div className="mt-12 grid grid-cols-3 gap-6">
            {[
              { value: "50K+", label: "Users" },
              { value: "$2M+", label: "Settled" },
              { value: "4.9★", label: "Rating" },
            ].map((stat) => (
              <div key={stat.label}>
                <p className="text-2xl font-bold text-primary-foreground">
                  {stat.value}
                </p>
                <p className="text-sm text-primary-foreground/70">{stat.label}</p>
              </div>
            ))}
          </div>
        </motion.div>

        {/* Right Panel - Form */}
        <div className="w-1/2 flex items-center justify-center p-8 lg:p-12">
          <div className="w-full max-w-md">
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3, duration: 0.5 }}
            >
              <h2 className="text-2xl font-display font-bold text-foreground">
                Create your account
              </h2>
              <p className="mt-2 text-muted-foreground">
                Get started for free. No credit card required.
              </p>

              <form onSubmit={handleSubmit} className="mt-8 space-y-5">
                <InputField
                  label="Full Name"
                  type="text"
                  placeholder="John Doe"
                  icon={<User size={18} />}
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  error={errors.name}
                />
                
                <InputField
                  label="Email"
                  type="email"
                  placeholder="you@example.com"
                  icon={<Mail size={18} />}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  error={errors.email}
                />
                
                <InputField
                  label="Phone Number"
                  type="tel"
                  placeholder="+1234567890"
                  icon={<Phone size={18} />}
                  value={phNumber}
                  onChange={(e) => setPhNumber(e.target.value)}
                  error={errors.phNumber}
                />
                
                <InputField
                  label="Password"
                  type="password"
                  placeholder="••••••••"
                  icon={<Lock size={18} />}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  error={errors.password}
                />

                <AuthButton 
                  type="submit" 
                  isLoading={isLoading}
                >
                  Create Account
                </AuthButton>
              </form>

              <Divider text="or continue with" />

              <SocialButton
                icon={
                  <svg className="w-5 h-5" viewBox="0 0 24 24">
                    <path
                      fill="currentColor"
                      d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                    />
                    <path
                      fill="currentColor"
                      d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                    />
                    <path
                      fill="currentColor"
                      d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                    />
                    <path
                      fill="currentColor"
                      d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                    />
                  </svg>
                }
              >
                Continue with Google
              </SocialButton>

              <p className="mt-8 text-center text-sm text-muted-foreground">
                Already have an account?{" "}
                <Link to="/login" className="text-primary font-semibold hover:underline">
                  Sign in
                </Link>
              </p>

              <p className="mt-4 text-center text-xs text-muted-foreground">
                By signing up, you agree to our{" "}
                <a href="#" className="text-primary hover:underline">
                  Terms
                </a>{" "}
                and{" "}
                <a href="#" className="text-primary hover:underline">
                  Privacy Policy
                </a>
              </p>
            </motion.div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signup;