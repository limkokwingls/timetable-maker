import { Box, Button, Grommet, Grid, FormField, Footer, Table } from 'grommet';
import { View, Github } from 'grommet-icons';
import { grommet } from 'grommet/themes';
import TableIndex from './table/TableIndex';
import ComboBox from './ui/ComboBox';

const lecturers: string[] = ['Liphoto', 'Molise', 'Molemo'];
const classes: string[] = ['BSITY1S1', 'BSSMY2S3', 'INTY1S1'];
const venues: string[] = ['MM1', 'MM2', 'MM3', 'MM4', 'Room 6'];

function App() {
  const comboBoxPad = {
    horizontal: 'medium',
    vertical: 'none',
  };
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
        <Box gridArea='nav' background='light-2'>
          <Box pad={comboBoxPad} direction='row'>
            <FormField label='Lecturer' htmlFor='lecturer'>
              <ComboBox options={lecturers} />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
          <Box pad={comboBoxPad} direction='row'>
            <FormField label='Class' htmlFor='students'>
              <ComboBox options={classes} id='students' />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
          <Box pad={comboBoxPad} direction='row'>
            <FormField label='Course' htmlFor='course'>
              <ComboBox options={classes} id='course' />
            </FormField>
          </Box>

          <Box pad={comboBoxPad} direction='row'>
            <FormField label='Venue' htmlFor='venues'>
              <ComboBox options={venues} id='venues' />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
        </Box>
        <Box gridArea='main' background='light-1'>
          <TableIndex />
        </Box>
        <Footer gridArea='footer' background='light-5' pad='small'>
          <Github size='medium' />
        </Footer>
      </Grid>
    </Grommet>
  );
}

export default App;
