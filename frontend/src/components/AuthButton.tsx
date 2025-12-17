import { motion } from "framer-motion";
import { ReactNode } from "react";
import { Loader2 } from "lucide-react";

interface AuthButtonProps {
  children: ReactNode;
  variant?: "primary" | "secondary" | "outline" | "ghost";
  isLoading?: boolean;
  icon?: ReactNode;
  type?: "button" | "submit" | "reset";
  disabled?: boolean;
  className?: string;
  onClick?: () => void;
}

const AuthButton = ({
  children,
  variant = "primary",
  isLoading = false,
  icon,
  className = "",
  disabled,
  type = "button",
  onClick,
}: AuthButtonProps) => {
  const baseStyles = `
    w-full py-3 px-4 rounded-xl font-semibold text-base
    flex items-center justify-center gap-2
    transition-all duration-200 ease-out
    disabled:opacity-50 disabled:cursor-not-allowed
    focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary/50
  `;

  const variantStyles = {
    primary: "gradient-primary text-primary-foreground shadow-md hover:shadow-lg hover:shadow-primary/25",
    secondary: "bg-secondary text-secondary-foreground hover:opacity-90",
    outline: "border-2 border-primary text-primary bg-transparent hover:bg-primary/5",
    ghost: "bg-transparent text-foreground hover:bg-muted",
  };

  return (
    <motion.button
      type={type}
      className={`${baseStyles} ${variantStyles[variant]} ${className}`}
      whileHover={{ scale: disabled || isLoading ? 1 : 1.02 }}
      whileTap={{ scale: disabled || isLoading ? 1 : 0.98 }}
      disabled={disabled || isLoading}
      onClick={onClick}
    >
      {isLoading ? (
        <Loader2 className="w-5 h-5 animate-spin" />
      ) : (
        <>
          {icon}
          {children}
        </>
      )}
    </motion.button>
  );
};

export default AuthButton;