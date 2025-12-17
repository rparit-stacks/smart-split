import { motion } from "framer-motion";
import { ReactNode } from "react";

interface AuthCardProps {
  children: ReactNode;
  className?: string;
}

const AuthCard = ({ children, className = "" }: AuthCardProps) => {
  return (
    <motion.div
      className={`bg-card rounded-2xl shadow-xl p-6 sm:p-8 w-full max-w-md mx-auto ${className}`}
      initial={{ opacity: 0, y: 20, scale: 0.98 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      transition={{ duration: 0.4, ease: [0.25, 0.46, 0.45, 0.94] }}
    >
      {children}
    </motion.div>
  );
};

export default AuthCard;