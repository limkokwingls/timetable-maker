import { Card, Grid, Text } from 'grommet';

function TableIndex() {
  const cards = Array(10)
    .fill(0)
    .map((_, i) => <Text key={i}>{`Card ${i}`}</Text>);
  return (
    <>
      <Grid columns={['15%', '15%', '15%', '15%']} pad='medium' gap='small'>
        {cards.map((card, index) => (
          <Card pad='large' key={index}>
            {card}
          </Card>
        ))}
      </Grid>
    </>
  );
}

export default TableIndex;
