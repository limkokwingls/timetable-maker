import {
  Box,
  Button,
  Layer,
  Collapsible,
  Heading,
  Grommet,
  Main,
  Paragraph,
  Grid,
  FormField,
  Select,
  Footer,
  Text,
} from 'grommet';
import { Notification, FormClose, View, Github } from 'grommet-icons';
import { grommet } from 'grommet/themes';
import React, { useState } from 'react';

const theme = {
  global: {
    colors: {
      brand: '#228BE6',
    },
    font: {
      family: 'Roboto',
      size: '18px',
      height: '20px',
    },
  },
};

// the prefix name of the Create option entry
const prefix = 'Create';

const defaultOptions: string[] = [];
for (let i = 1; i <= 5; i += 1) {
  defaultOptions.push(`option ${i}`);
}

const updateCreateOption = (text: string) => {
  const len = defaultOptions.length;
  if (defaultOptions[len - 1].includes(prefix)) {
    // remove Create option before adding an updated one
    defaultOptions.pop();
  }
  defaultOptions.push(`${prefix} '${text}'`);
};

// improving Search support of special characters
const getRegExp = (text: string) => {
  // The line below escapes regular expression special characters:
  // [ \ ^ $ . | ? * + ( )
  const escapedText = text.replace(/[-\\^$*+?.()|[\]{}]/g, '\\$&');

  // Create the regular expression with modified value which
  // handles escaping special characters. Without escaping special
  // characters, errors will appear in the console
  return new RegExp(escapedText, 'i');
};

function App() {
  const [options, setOptions] = useState(defaultOptions);
  const [value, setValue] = useState('');
  const [searchValue, setSearchValue] = useState('');

  return (
    <Grommet full theme={grommet}>
      <Grid
        fill
        rows={['xxsmall', 'full', 'xxsmall']}
        columns={['medium', 'full', 'full']}
        gap='xxsmall'
        areas={[
          { name: 'header', start: [0, 0], end: [1, 0] },
          { name: 'nav', start: [0, 1], end: [0, 1] },
          { name: 'main', start: [1, 1], end: [1, 1] },
          { name: 'footer', start: [0, 2], end: [1, 2] },
        ]}
      >
        <Box gridArea='header' background='brand' />
        <Box gridArea='nav' background='light-1'>
          <Box pad='medium' direction='row'>
            <FormField label='Lecturer' htmlFor='lecturer'>
              <Select
                open
                size='medium'
                placeholder='Select'
                value={value}
                options={options}
                onChange={({ option }) => {
                  if (option.includes(prefix)) {
                    defaultOptions.pop(); // remove Create option
                    defaultOptions.push(searchValue);
                    setValue(searchValue);
                  } else {
                    setValue(option);
                  }
                }}
                onClose={() => setOptions(defaultOptions)}
                onSearch={(text: string) => {
                  updateCreateOption(text);
                  const exp = getRegExp(text);
                  setOptions(defaultOptions.filter((o) => exp.test(o)));
                  setSearchValue(text);
                }}
              />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
        </Box>
        <Box gridArea='main' background='light-3' />
        <Footer gridArea='footer' background='light-5' pad='small'>
          <Github size='medium' />
        </Footer>
      </Grid>
    </Grommet>
  );
}

export default App;
