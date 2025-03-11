class Parser:
    def __init__(self, file_path):
        self.file_path = file_path
        self.current_command = None
        self.current_line = 0
        with open(file_path, 'r') as file:
            self.commands = [line.strip() for line in file]
            self.commands = [cmd.split('//')[0].strip() for cmd in self.commands]
            self.commands = [cmd for cmd in self.commands if cmd]
    
    def has_more_commands(self):
        return self.current_line < len(self.commands)
    
    def advance(self):
        if self.has_more_commands():
            self.current_command = self.commands[self.current_line]
            self.current_line += 1
    
    def command_type(self):
        command = self.current_command.strip()
        if command.startswith('(') and command.endswith(')'):
            return 'L_COMMAND'
        elif command.startswith('@'):
            return 'A_COMMAND'
        else:
            return 'C_COMMAND'
    
    def symbol(self):
        if self.command_type() == 'A_COMMAND':
            return self.current_command[1:]
        elif self.command_type() == 'L_COMMAND':
            return self.current_command[1:-1]
        return None
    
    def dest(self):
        if '=' in self.current_command:
            return self.current_command.split('=')[0]
        return 'null'
    
    def comp(self):
        if '=' in self.current_command:
            command = self.current_command.split('=')[1]
        else:
            command = self.current_command
        if ';' in command:
            return command.split(';')[0]
        return command
    
    def jump(self):
        if ';' in self.current_command:
            return self.current_command.split(';')[1]
        return 'null'