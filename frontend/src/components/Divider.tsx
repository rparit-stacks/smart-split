interface DividerProps {
  text?: string;
}

const Divider = ({ text }: DividerProps) => {
  return (
    <div className="flex items-center gap-4 my-6">
      <div className="flex-1 h-px bg-border" />
      {text && (
        <span className="text-sm text-muted-foreground font-medium">{text}</span>
      )}
      <div className="flex-1 h-px bg-border" />
    </div>
  );
};

export default Divider;