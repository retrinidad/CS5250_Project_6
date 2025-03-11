from parser import Parser
from binary_code import BinaryCode
from symbol_table import SymbolTable

def assembler(input_file, output_file):
    #First pass
    parser = Parser(input_file)
    symbol_table = SymbolTable()
    instruction_counter = 0

    while parser.has_more_commands():
        parser.advance()
        command_type = parser.command_type()
        
        if command_type == 'L_COMMAND':
            symbol = parser.symbol()
            symbol_table.add_entry(symbol, instruction_counter)
        else:
            instruction_counter += 1

    # Second pass
    parser = Parser(input_file)
    code = BinaryCode()
    binary_instructions = []

    while parser.has_more_commands():
        parser.advance()
        command_type = parser.command_type()

        if parser.command_type() == 'A_COMMAND':
            symbol = parser.symbol()
            if symbol.isdigit():
                value = int(symbol)
            else:
                value = symbol_table.add_variable(symbol)
            binary = format(value, '016b')
            binary_instructions.append(binary)
            
        elif parser.command_type() == 'C_COMMAND':
            comp = code.comp(parser.comp())
            dest = code.dest(parser.dest())
            jump = code.jump(parser.jump())
            binary = f'111{comp}{dest}{jump}'
            binary_instructions.append(binary)

    with open(output_file, 'w', newline='') as f:
        for i, instruction in enumerate(binary_instructions):
            if i < len(binary_instructions) - 1:
                f.write(f"{instruction}\n")
            else:
                f.write(instruction)

if __name__ == '__main__':
    input_file = 'rect/Rect.asm'
    output_file = 'Rect1.hack'
    assembler(input_file, output_file)