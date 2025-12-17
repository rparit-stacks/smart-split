import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { User, Mail, Phone, MapPin, Calendar, Save, Loader2 } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { userApi, UserResponse, UserRequest } from "@/lib/api";
import { useAuth } from "@/contexts/AuthContext";
import DashboardLayout from "@/components/DashboardLayout";
import InputField from "@/components/InputField";
import AuthButton from "@/components/AuthButton";

const Settings = () => {
  const { user: authUser, login } = useAuth();
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [userData, setUserData] = useState<UserResponse | null>(null);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phNumber: "",
    address: "",
    age: 0,
    profileUrl: "",
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    loadUserData();
  }, []);

  const loadUserData = async () => {
    console.log('🔵 Settings: loadUserData called');
    setIsLoading(true);
    try {
      console.log('📤 Settings: Calling getCurrentUser API...');
      const response = await userApi.getCurrentUser();
      console.log('📥 Settings: getCurrentUser response:', response);
      if (response.error) {
        toast({
          title: "Error",
          description: response.error,
          variant: "destructive",
        });
      } else if (response.data) {
        setUserData(response.data);
        setFormData({
          name: response.data.name || "",
          email: response.data.email || "",
          phNumber: response.data.phNumber || "",
          address: response.data.address || "",
          age: response.data.age || 0,
          profileUrl: response.data.profileUrl || "",
        });
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load user data",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.name || formData.name.length < 2) {
      newErrors.name = "Name must be at least 2 characters";
    }

    if (!formData.email || !/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Please enter a valid email";
    }

    if (!formData.phNumber || formData.phNumber.length < 10) {
      newErrors.phNumber = "Please enter a valid phone number";
    }

    if (formData.age < 0 || formData.age > 150) {
      newErrors.age = "Please enter a valid age";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm() || !userData) return;

    setIsSaving(true);
    try {
      const updateData: UserRequest = {
        name: formData.name,
        email: formData.email,
        phNumber: formData.phNumber,
        address: formData.address || undefined,
        age: formData.age || undefined,
        profileUrl: formData.profileUrl || undefined,
      };

      const response = await userApi.updateUser(userData.id, updateData);

      if (response.error) {
        toast({
          title: "Update failed",
          description: response.error,
          variant: "destructive",
        });
      } else if (response.data) {
        toast({
          title: "Profile updated!",
          description: "Your profile has been updated successfully.",
        });
        
        // Update auth context if name changed
        if (response.data.name !== authUser?.name) {
          login(response.data.email, response.data.name);
        }
        
        setUserData(response.data);
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to update profile",
        variant: "destructive",
      });
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center min-h-screen">
          <Loader2 className="w-8 h-8 animate-spin text-primary" />
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      <div className="min-h-screen bg-background p-4 md:p-8">
      <div className="max-w-4xl mx-auto">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <h1 className="text-3xl font-display font-bold text-foreground mb-2">
            Settings
          </h1>
          <p className="text-muted-foreground mb-8">
            Manage your account settings and preferences
          </p>

          <div className="bg-card rounded-2xl shadow-md p-6 md:p-8">
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Profile Picture Section */}
              <div className="flex items-center gap-6 pb-6 border-b border-border">
                <div className="w-20 h-20 rounded-full gradient-primary flex items-center justify-center text-primary-foreground text-2xl font-bold">
                  {formData.name ? formData.name[0].toUpperCase() : "U"}
                </div>
                <div>
                  <h3 className="font-semibold text-foreground text-lg">
                    {formData.name || "User"}
                  </h3>
                  <p className="text-sm text-muted-foreground">{formData.email}</p>
                </div>
              </div>

              {/* Form Fields */}
              <div className="grid md:grid-cols-2 gap-6">
                <InputField
                  label="Full Name"
                  type="text"
                  placeholder="John Doe"
                  icon={<User size={18} />}
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                  error={errors.name}
                />

                <InputField
                  label="Email"
                  type="email"
                  placeholder="you@example.com"
                  icon={<Mail size={18} />}
                  value={formData.email}
                  onChange={(e) =>
                    setFormData({ ...formData, email: e.target.value })
                  }
                  error={errors.email}
                />

                <InputField
                  label="Phone Number"
                  type="tel"
                  placeholder="+1234567890"
                  icon={<Phone size={18} />}
                  value={formData.phNumber}
                  onChange={(e) =>
                    setFormData({ ...formData, phNumber: e.target.value })
                  }
                  error={errors.phNumber}
                />

                <InputField
                  label="Age"
                  type="number"
                  placeholder="25"
                  icon={<Calendar size={18} />}
                  value={formData.age.toString()}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      age: parseInt(e.target.value) || 0,
                    })
                  }
                  error={errors.age}
                />

                <div className="md:col-span-2">
                  <InputField
                    label="Address"
                    type="text"
                    placeholder="123 Main St, City, State"
                    icon={<MapPin size={18} />}
                    value={formData.address}
                    onChange={(e) =>
                      setFormData({ ...formData, address: e.target.value })
                    }
                    error={errors.address}
                  />
                </div>

                <div className="md:col-span-2">
                  <InputField
                    label="Profile Picture URL"
                    type="url"
                    placeholder="https://example.com/profile.jpg"
                    icon={<User size={18} />}
                    value={formData.profileUrl}
                    onChange={(e) =>
                      setFormData({ ...formData, profileUrl: e.target.value })
                    }
                    error={errors.profileUrl}
                  />
                </div>
              </div>

              {/* Save Button */}
              <div className="pt-6 border-t border-border">
                <AuthButton
                  type="submit"
                  isLoading={isSaving}
                  className="w-full md:w-auto min-w-[200px]"
                >
                  <Save size={18} />
                  Save Changes
                </AuthButton>
              </div>
            </form>
          </div>

          {/* Account Info */}
          {userData && (
            <motion.div
              className="mt-6 bg-card rounded-2xl shadow-md p-6"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
            >
              <h3 className="font-semibold text-foreground mb-4">Account Information</h3>
              <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">User ID:</span>
                  <span className="text-foreground font-mono text-xs">
                    {userData.id}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Role:</span>
                  <span className="text-foreground capitalize">{userData.role?.toLowerCase()}</span>
                </div>
              </div>
            </motion.div>
          )}
        </motion.div>
      </div>
      </div>
    </DashboardLayout>
  );
};

export default Settings;

