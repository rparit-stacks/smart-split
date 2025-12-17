import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { 
  Plus, 
  Users, 
  Receipt, 
  TrendingUp, 
  ArrowUpRight, 
  ArrowDownLeft,
  LogOut
} from "lucide-react";
import Logo from "@/components/Logo";
import AuthButton from "@/components/AuthButton";

const Dashboard = () => {
  const navigate = useNavigate();

  const balances = [
    { name: "Alex", amount: 45.50, type: "owe" },
    { name: "Sarah", amount: 23.00, type: "owed" },
    { name: "Mike", amount: 12.75, type: "owe" },
  ];

  const recentActivity = [
    { title: "Dinner at Olive Garden", amount: 85.00, date: "Today", paidBy: "You" },
    { title: "Uber ride", amount: 24.50, date: "Yesterday", paidBy: "Alex" },
    { title: "Groceries", amount: 156.30, date: "Dec 15", paidBy: "Sarah" },
  ];

  const container = {
    hidden: { opacity: 0 },
    show: {
      opacity: 1,
      transition: { staggerChildren: 0.1 }
    }
  };

  const item = {
    hidden: { opacity: 0, y: 20 },
    show: { opacity: 1, y: 0 }
  };

  return (
    <div className="min-h-screen bg-background safe-area-inset">
      {/* Header */}
      <motion.header
        className="gradient-primary px-4 pt-4 pb-8 md:px-8"
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="max-w-6xl mx-auto">
          <div className="flex items-center justify-between">
            <Logo size="md" />
            <button 
              onClick={() => navigate("/login")}
              className="p-2 rounded-full bg-primary-foreground/10 hover:bg-primary-foreground/20 transition-colors"
            >
              <LogOut size={20} className="text-primary-foreground" />
            </button>
          </div>
          
          <div className="mt-8 text-center md:text-left">
            <p className="text-primary-foreground/80">Your balance</p>
            <h1 className="text-4xl md:text-5xl font-display font-bold text-primary-foreground mt-1">
              $156.25
            </h1>
            <p className="text-sm text-primary-foreground/70 mt-2">
              You owe <span className="text-primary-foreground">$58.25</span> • 
              You are owed <span className="text-primary-foreground">$214.50</span>
            </p>
          </div>
        </div>
      </motion.header>

      {/* Quick Actions */}
      <div className="px-4 md:px-8 -mt-4">
        <div className="max-w-6xl mx-auto">
          <motion.div 
            className="grid grid-cols-3 gap-3"
            variants={container}
            initial="hidden"
            animate="show"
          >
            {[
              { icon: Plus, label: "Add Expense", color: "gradient-primary" },
              { icon: Users, label: "New Group", color: "bg-secondary" },
              { icon: Receipt, label: "Settle Up", color: "bg-accent" },
            ].map((action) => (
              <motion.button
                key={action.label}
                variants={item}
                className={`${action.color} p-4 rounded-2xl shadow-lg flex flex-col items-center gap-2 hover:scale-105 transition-transform`}
                whileTap={{ scale: 0.95 }}
              >
                <action.icon size={24} className={action.color === "bg-accent" ? "text-accent-foreground" : "text-primary-foreground"} />
                <span className={`text-xs font-medium ${action.color === "bg-accent" ? "text-accent-foreground" : "text-primary-foreground"}`}>
                  {action.label}
                </span>
              </motion.button>
            ))}
          </motion.div>
        </div>
      </div>

      {/* Main Content */}
      <div className="px-4 md:px-8 py-6">
        <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-6">
          {/* Balances */}
          <motion.div
            className="bg-card rounded-2xl p-5 shadow-md"
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.2 }}
          >
            <div className="flex items-center justify-between mb-4">
              <h2 className="font-display font-semibold text-foreground">Balances</h2>
              <TrendingUp size={20} className="text-muted-foreground" />
            </div>
            
            <div className="space-y-3">
              {balances.map((balance, index) => (
                <motion.div
                  key={balance.name}
                  className="flex items-center justify-between p-3 rounded-xl bg-muted/50"
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.3 + index * 0.1 }}
                >
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full gradient-primary flex items-center justify-center text-primary-foreground font-semibold">
                      {balance.name[0]}
                    </div>
                    <span className="font-medium text-foreground">{balance.name}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    {balance.type === "owe" ? (
                      <ArrowUpRight size={16} className="text-destructive" />
                    ) : (
                      <ArrowDownLeft size={16} className="text-green-500" />
                    )}
                    <span className={`font-semibold ${balance.type === "owe" ? "text-destructive" : "text-green-500"}`}>
                      ${balance.amount.toFixed(2)}
                    </span>
                  </div>
                </motion.div>
              ))}
            </div>
          </motion.div>

          {/* Recent Activity */}
          <motion.div
            className="bg-card rounded-2xl p-5 shadow-md"
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.3 }}
          >
            <div className="flex items-center justify-between mb-4">
              <h2 className="font-display font-semibold text-foreground">Recent Activity</h2>
              <Receipt size={20} className="text-muted-foreground" />
            </div>
            
            <div className="space-y-3">
              {recentActivity.map((activity, index) => (
                <motion.div
                  key={activity.title}
                  className="flex items-center justify-between p-3 rounded-xl bg-muted/50"
                  initial={{ opacity: 0, x: 10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: 0.4 + index * 0.1 }}
                >
                  <div>
                    <p className="font-medium text-foreground">{activity.title}</p>
                    <p className="text-sm text-muted-foreground">
                      {activity.date} • Paid by {activity.paidBy}
                    </p>
                  </div>
                  <span className="font-semibold text-foreground">
                    ${activity.amount.toFixed(2)}
                  </span>
                </motion.div>
              ))}
            </div>
          </motion.div>
        </div>
      </div>

      {/* Floating Action Button - Mobile */}
      <motion.button
        className="md:hidden fixed bottom-6 right-6 w-14 h-14 gradient-primary rounded-full shadow-xl flex items-center justify-center"
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        initial={{ opacity: 0, scale: 0 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.5, type: "spring" }}
      >
        <Plus size={24} className="text-primary-foreground" />
      </motion.button>
    </div>
  );
};

export default Dashboard;