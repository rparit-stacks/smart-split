import { motion } from "framer-motion";
import { ReactNode } from "react";

interface SocialButtonProps {
  children: ReactNode;
  icon: ReactNode;
  className?: string;
  onClick?: () => void;
}

const SocialButton = ({
  children,
  icon,
  className = "",
  onClick,
}: SocialButtonProps) => {
  return (
    <motion.button
      type="button"
      className={`
        w-full py-3 px-4 rounded-xl font-medium text-sm
        flex items-center justify-center gap-3
        bg-card border border-border text-foreground
        hover:bg-muted transition-colors duration-200
        focus:outline-none focus:ring-2 focus:ring-primary/30
        ${className}
      `}
      whileHover={{ scale: 1.02 }}
      whileTap={{ scale: 0.98 }}
      onClick={onClick}
    >
      {icon}
      {children}
    </motion.button>
  );
};

export default SocialButton;